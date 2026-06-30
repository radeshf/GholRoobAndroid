package com.mrprojects.gholrob.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mrprojects.gholrob.repository.api.ApiHelper

class GameViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GameViewModel(apiHelper) as T
    }
}