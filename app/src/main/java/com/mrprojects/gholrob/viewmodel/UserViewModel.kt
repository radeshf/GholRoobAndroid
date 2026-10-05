package com.mrprojects.gholrob.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrprojects.gholrob.model.ErrorTypes
import com.mrprojects.gholrob.model.User
import com.mrprojects.gholrob.repository.db.dao.UserDao
import com.mrprojects.gholrob.viewmodel.user.BonusCoinTimerManager
import com.mrprojects.gholrob.viewmodel.user.BonusEyeTimerManager
import com.mrprojects.gholrob.viewmodel.user.BonusEnergyTimerManager
import com.mrprojects.gholrob.viewmodel.user.BonusShieldTimerManager
import com.mrprojects.gholrob.viewmodel.user.HeartTimerManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch



class UserViewModel(private val dao: UserDao) : ViewModel(){

    val user = MutableLiveData<User>()

    val heartTimer = HeartTimerManager(dao, viewModelScope) { updated ->
        user.postValue(updated)
    }
    val bonusEnergyTimer = BonusEnergyTimerManager(dao, viewModelScope) { updated ->
        user.postValue(updated)
    }

    val bonusCoinTimer = BonusCoinTimerManager(dao, viewModelScope) { updated ->
        user.postValue(updated)
    }
    val bonusEyeTimer = BonusEyeTimerManager(dao, viewModelScope) { updated ->
        user.postValue(updated)
    }
    val bonusShieldTimer = BonusShieldTimerManager(dao, viewModelScope) { updated ->
        user.postValue(updated)
    }
    private val _errorEvent = MutableSharedFlow<ErrorTypes>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val errorEvent = _errorEvent.asSharedFlow()

    fun loadUser() {
        viewModelScope.launch {
            val u = dao.getUser()
            u?.let {
                user.postValue(it)
            }
        }
    }

    fun storeUser(u: User) {
        viewModelScope.launch {
            dao.deleteAllUsers()
            dao.insert(u)
            val updatedUser = dao.getUser()
            updatedUser?.let {
                user.postValue(it)
                heartTimer.start(it)
                bonusEnergyTimer.start(it)
                bonusCoinTimer.start(it)
                bonusEyeTimer.start(it)
                bonusShieldTimer.start(it)
            }
        }
    }


    fun useLife() {
        val u = user.value ?: return
        u.lives -= 1
        viewModelScope.launch { dao.update(u) }
        user.postValue(u)
    }

    fun useEye() {
        val u = user.value ?: return
        u.eyes -= 1
        viewModelScope.launch { dao.update(u) }
        user.postValue(u)
    }

    fun useShield() {
        val u = user.value ?: return
        u.shields -= 1
        viewModelScope.launch { dao.update(u) }
        user.postValue(u)
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

    fun checkUserLastBuyEnergyTime(): Boolean {
        val u = user.value ?: return false
        if (bonusEnergyTimer.isRunning ) {
            viewModelScope.launch { _errorEvent.emit(ErrorTypes.MAX_BONUS_ENERGY_REACHED) }
            return false
        }
        return true
    }

    fun checkUserLastBonusCoin(): Boolean {
        val u = user.value ?: return false
        if (bonusCoinTimer.isRunning ) {
            viewModelScope.launch { _errorEvent.emit(ErrorTypes.MAX_BONUS_COIN_REACHED) }
            return false
        }
        return true
    }

    fun checkUserLastBonusEye(): Boolean {
        val u = user.value ?: return false
        if (bonusEyeTimer.isRunning ) {
            viewModelScope.launch { _errorEvent.emit(ErrorTypes.MAX_BONUS_EYE_REACHED) }
            return false
        }
        return true
    }


    fun checkUserLastBonusShield(): Boolean {
        val u = user.value ?: return false
        if (bonusShieldTimer.isRunning) {
            viewModelScope.launch { _errorEvent.emit(ErrorTypes.MAX_BONUS_SHIELD_REACHED) }
            return false
        }
        return true
    }


    override fun onCleared() {
        super.onCleared()
        heartTimer.stop()
        bonusEnergyTimer.stop()
        bonusCoinTimer.stop()
        bonusEyeTimer.stop()
        bonusShieldTimer.stop()
    }


}
