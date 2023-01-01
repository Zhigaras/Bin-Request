package com.zhigaras.binrequest.presentation

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModel
import com.zhigaras.binrequest.repository.MainRepository
import javax.inject.Inject

class ViewModelFactory @Inject constructor(
    private val mainRepository: MainRepository,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(application, mainRepository) as T
        }
        throw java.lang.IllegalArgumentException("Unknown class name")
    }
}