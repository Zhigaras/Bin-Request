package com.zhigaras.binrequest.presentation

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModel
import com.zhigaras.binrequest.repository.RemoteRepository

class MainViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(RemoteRepository()) as T
        }
        throw java.lang.IllegalArgumentException("Unknown class name")
    }
}