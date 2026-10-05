package com.mrprojects.gholrob.viewmodel.user

import androidx.lifecycle.MutableLiveData
import com.mrprojects.gholrob.model.User
import com.mrprojects.gholrob.repository.db.dao.UserDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BonusEnergyTimerManager(
    private val dao: UserDao,
    private val scope: CoroutineScope,
    private val onUserChanged: (User) -> Unit
) {


    val timer = MutableLiveData<TimerState>()

    private var timerJob: Job? = null

    val isRunning: Boolean
        get() = timerJob?.isActive == true

    fun start(u: User) {
        if (u.nextBonusHeartTime > 0) {
            startBonusHeartTimer()
        }else{
            stop()
        }
    }

    private fun startBonusHeartTimer() {
        timerJob?.cancel()
        timerJob = scope.launch {
            val u = dao.getUser() ?: return@launch
            val targetTime = System.currentTimeMillis() + (u.nextBonusHeartTime * 1000)
            while (u.nextBonusHeartTime > 0) {
                val remaining = (targetTime - System.currentTimeMillis()).coerceAtLeast(0L)
                val totalTime = u.heartRefillInterval
                val percent = if (totalTime > 0L) (((totalTime - remaining).toFloat() / totalTime) * 100).toInt().coerceIn(0, 100) else 0
                timer.postValue(TimerState(remaining, percent))

                if (remaining <= 0) {
                    u.nextBonusHeartTime = 0L
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
