package com.zhigaras.binrequest.presentation

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModel
import com.zhigaras.binrequest.repository.MainRepository
import javax.inject.Inject

class ViewModelFactory @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(mainRepository) as T
        }
        throw java.lang.IllegalArgumentException("Unknown class name")
    }
}