package com.mrprojects.gholrob.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrprojects.gholrob.model.Attempt
import com.mrprojects.gholrob.repository.api.ApiHelper
import kotlinx.coroutines.launch

class GameViewModel(val apiHelper: ApiHelper) : ViewModel() {
    private val _attempt = MutableLiveData<Attempt>()
    val attempt: LiveData<Attempt> = _attempt

    fun fetchGame(gameId: Int) {
        viewModelScope.launch {
            apiHelper.getGame(gameId) { attempt ->
                _attempt.postValue(attempt)
            }
        }
    }
}