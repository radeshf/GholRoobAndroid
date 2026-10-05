package com.mrprojects.gholrob.viewmodel.user

import androidx.lifecycle.MutableLiveData
import com.mrprojects.gholrob.model.User
import com.mrprojects.gholrob.repository.db.dao.UserDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HeartTimerManager(
    private val dao: UserDao,
    private val scope: CoroutineScope,
    private val onUserChanged: (User) -> Unit
) {

    val timer = MutableLiveData<Long>()

    private var timerJob: Job? = null


    fun start(u: User) {
        if (u.currentHearts < u.maxHearts) {
            startHeartRegenTimer()
        }else{
            stop()
        }
    }

    fun stop() {
        timerJob?.cancel()
        timerJob = null
        timer.postValue(0L)
    }

    private fun startHeartRegenTimer() {
        timerJob?.cancel()
        timerJob = scope.launch {
            var u = dao.getUser()
            u?.let {
                while (u.currentHearts < u.maxHearts) {
                    val now = System.currentTimeMillis()
                    val remaining = (u.nextHeartTimeMillis() - now).coerceAtLeast(0L)
                    timer.postValue(remaining)

                    if (remaining <= 0) {
                        u.currentHearts = u.maxHearts
                        u.nextHeartTime = 0L
                        stop()
                        onUserChanged(u)
                        dao.update(u)
                    }

                    delay(1000)
                    if (u.currentHearts >= u.maxHearts) {
                        stop()
                        break
                    }
                }

            }
        }
    }


}
