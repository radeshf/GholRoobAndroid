package com.mrprojects.gholrob.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mrprojects.gholrob.repository.db.dao.UserDao

class UserViewModelFactory(private val userDao: UserDao) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(userDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}