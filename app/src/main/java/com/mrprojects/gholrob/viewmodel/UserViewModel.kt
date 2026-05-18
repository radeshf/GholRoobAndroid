package com.mrprojects.gholrob.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrprojects.gholrob.model.ErrorTypes
import com.mrprojects.gholrob.model.User
import com.mrprojects.gholrob.repository.db.dao.UserDao
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlin.text.insert

class UserViewModel(
    private val dao: UserDao,
) : ViewModel() {

    val user = MutableLiveData<User>()
    val nextHeartTimer = MutableLiveData<Long>()
    private var timerJob: Job? = null

    private val _errorEvent = MutableSharedFlow<ErrorTypes>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val errorEvent = _errorEvent.asSharedFlow()

    fun loadUser() {
        viewModelScope.launch {
            val u = dao.getUser()
            user.postValue(u)
        }
    }

    fun storeUser(u: User) {
        viewModelScope.launch {
            dao.deleteAllUsers()
            dao.insert(u)
            val updatedUser = dao.getUser()
            updatedUser?.let {
                user.postValue(it)
                startHeartRegenIfNeeded(it)
            }
        }
    }


    fun spendHeart() {
        val u = user.value ?: return
        if (u.currentHearts > 0) {
            u.currentHearts -= 1
            if (u.currentHearts < u.maxHearts && (u.nextHeartTimeMillis() == 0L || u.nextHeartTimeMillis() <= System.currentTimeMillis())) {
                u.nextHeartTime = System.currentTimeMillis() + HEART_REGEN_TIME
                startHeartRegenTimer()
            }
            viewModelScope.launch { dao.update(u) }
            user.postValue(u)
        }
    }

    private fun startHeartRegenIfNeeded(u: User) {
        if (u.currentHearts < u.maxHearts && u.nextHeartTimeMillis() > System.currentTimeMillis()) {
            startHeartRegenTimer()
        }
    }

    private fun startHeartRegenTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            var u = dao.getUser()

            while (u.currentHearts < u.maxHearts) {
                val now = System.currentTimeMillis()
                val remaining = (u.nextHeartTimeMillis() - now).coerceAtLeast(0L)
                nextHeartTimer.postValue(remaining)
                var isUserChanged = false
                if (remaining <= 0) {
                    isUserChanged = true
                    u.currentHearts += 1
                    if (u.currentHearts < u.maxHearts) {
                        u.nextHeartTime = System.currentTimeMillis() + HEART_REGEN_TIME
                    } else {
                        u.nextHeartTime = 0L
                        stopTimer()
                    }
                }
                if (isUserChanged) {
                    user.postValue(u)
                    dao.update(u)
                }

                delay(1000)
                if (u.currentHearts >= u.maxHearts) {
                    stopTimer()
                    break // exit loop
                }
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
        nextHeartTimer.postValue(0L)
    }



    fun addHeart() {
        val u = user.value ?: return
        if (u.currentHearts < u.maxHearts) {
            u.currentHearts += 1
            if (u.currentHearts >= u.maxHearts) {
                removeTimer(u)
            }
            viewModelScope.launch { dao.update(u) }
            user.postValue(u)
        }
    }

    fun removeTimer(u: User) {
        u.nextHeartTime = 0L
        stopTimer()
    }

    fun fullChargeHearts() {
        val u = user.value ?: return
        if (u.currentHearts >= u.maxHearts) return

        u.currentHearts = u.maxHearts

        removeTimer(u)

        viewModelScope.launch { dao.update(u) }
        user.postValue(u)
    }

    fun addExtraHeart() {
        val u = user.value ?: return
        u.maxHearts += 1
        viewModelScope.launch { dao.update(u) }
        user.postValue(u)
        fullChargeHearts()
    }


    fun removeExtraHeart() {
        val u = user.value ?: return
        u.maxHearts -= 1
        viewModelScope.launch { dao.update(u) }
        user.postValue(u)
    }

    fun addCoins(amount: Int) {
        val u = user.value ?: return
        u.coins += amount
        viewModelScope.launch { dao.update(u) }
        user.postValue(u)
    }

    fun addLife(amount: Int) {
        val u = user.value ?: return
        u.lives += amount
        viewModelScope.launch { dao.update(u) }
        user.postValue(u)
    }

    fun useLife() {
        val u = user.value ?: return
        u.lives -= 1
        viewModelScope.launch { dao.update(u) }
        user.postValue(u)
    }


    fun spendCoins(amount: Int) {
        val u = user.value ?: return
        u.coins -= amount
        viewModelScope.launch { dao.update(u) }
        user.postValue(u)
    }


    fun isUserNeedHearts(): Boolean {
        return user.value!!.currentHearts <= 0
    }

    fun isUserHasHearts(): Boolean {
        return user.value!!.currentHearts > 0
    }


    fun checkUserCoin(amount: Int): Boolean {
        val u = user.value ?: return false
        if (u.coins < amount) {
            viewModelScope.launch { _errorEvent.emit(ErrorTypes.COIN_NOT_ENOUGH) }
            return false
        }
        return true
    }

    fun checkUserHeartBeforeFill(): Boolean {
        val u = user.value ?: return false
        if (u.currentHearts == u.maxHearts) {
            viewModelScope.launch { _errorEvent.emit(ErrorTypes.HEARTS_ARE_FULL) }
            return false
        }
        return true
    }

    fun checkUserHeartBeforeBuy(): Boolean {
        val u = user.value ?: return false
        if (u.maxHearts >= 5) {
            viewModelScope.launch { _errorEvent.emit(ErrorTypes.MAX_HEART_REACHED) }
            return false
        }
        return true
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        timerJob = null
    }

    companion object {
        private const val HEART_REGEN_TIME = 15 * 60 * 1000L
    }
}
