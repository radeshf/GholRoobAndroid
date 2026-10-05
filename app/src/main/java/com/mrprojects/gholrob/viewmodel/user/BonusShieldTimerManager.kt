package com.mrprojects.gholrob.viewmodel.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mrprojects.gholrob.model.User
import com.mrprojects.gholrob.repository.db.dao.UserDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BonusShieldTimerManager(
    private val dao: UserDao,
    private val scope: CoroutineScope,
    private val onUserChanged: (User) -> Unit
) {


    val timer = MutableLiveData<TimerState>()
    private var timerJob: Job? = null


    val isRunning: Boolean
        get() = timerJob?.isActive == true

    fun start(u: User) {
        if (u.nextBonusShieldTime > 0) {
            startTimer()
        }else{
            stop()
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = scope.launch {
            val u = dao.getUser() ?: return@launch
            val targetTime = System.currentTimeMillis() + (u.nextBonusShieldTime * 1000L)
            while (u.nextBonusShieldTime > 0) {
                val remaining = (targetTime - System.currentTimeMillis()).coerceAtLeast(0L)
                val day = 24 * 60 * 60 * 1000L
                val percent = (((day - remaining).coerceAtLeast(0L).toFloat() / day) * 100).toInt().coerceIn(0, 100)
                timer.postValue(TimerState(remaining, percent))

                if (remaining <= 0) {
                    u.nextBonusShieldTime = 0L
                    dao.update(u)
                    onUserChanged(u)
                    break
                }

                delay(1000)
            }


        }
    }

    fun stop() {
        timerJob?.cancel()
        timerJob = null
        timer.postValue(TimerState(0L, 0))
    }

}
