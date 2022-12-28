package com.zhigaras.binrequest.presentation

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zhigaras.binrequest.model.BinReplyModel
import com.zhigaras.binrequest.repository.RemoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val remoteRepository: RemoteRepository
) : ViewModel() {
    
    private val _replyFlow = MutableStateFlow<BinReplyModel?>(null)
    val replyFlow = _replyFlow.asStateFlow()
    
    private val _requestErrorChannel = Channel<String>()
    val requestErrorChannel = _requestErrorChannel.receiveAsFlow()
    
    private val _inputErrorFlow = MutableStateFlow<String?>(null)
    val inputErrorFlow = _inputErrorFlow.asStateFlow()
    
    private var _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    
    private var _intentChannel = Channel<Intent>()
    val intentChannel = _intentChannel.receiveAsFlow()
    
    fun checkBin(number: String) {
        
//        viewModelScope.launch(Dispatchers.IO) {
//            val response = remoteRepository.binRequestApi.getBinInfo(number)
//            if (response.isSuccessful) {
//                _replyFlow.value = response.body()
//            } else {
//                _errorChannel.send(response.errorBody().toString())
//            }
//        }
        
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                _isLoading.value = true
                remoteRepository.binRequestApi.getBinInfo(number)
            }.fold(
                onSuccess = { _replyFlow.value = it.body()},
                onFailure = { _requestErrorChannel.send(it.message.toString()) }
            )
            _isLoading.value = false
        }
    }
    
    fun validateInput(input: CharSequence): Boolean {
        val isValid = input.length >= 4
        if (isValid) _inputErrorFlow.value = null
        else {
            if (input.isEmpty()) _inputErrorFlow.value = null
            else _inputErrorFlow.value = "Input at least 4 numbers"
        }
        return isValid
    }
    
    fun getExternalIntent(intent: Intent) {
        Log.d(TAG, "viewModel send ${intent.toString()}")
        viewModelScope.launch {/**Dispatcher??????*/
            _intentChannel.send(intent)
        }
    }
    
//    fun startCallIntent(context: Context, phoneNumber: String) {
//        val numberToCall = Uri.parse(phoneNumber.toString())
//        val callIntent = Intent(Intent.ACTION_DIAL, numberToCall)
//        try {
//            context.startActivity(callIntent)
//        } catch (e: ActivityNotFoundException) {
//
//        }
//    }
//
//    fun onPhoneNumberClick() {
//
//    }
}