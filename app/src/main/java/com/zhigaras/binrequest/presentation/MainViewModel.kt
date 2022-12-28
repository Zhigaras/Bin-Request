package com.zhigaras.binrequest.presentation

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
    
    private val emptyReply = BinReplyModel()
    
    private val _replyFlow = MutableStateFlow<BinReplyModel?>(null)
    val replyFlow = _replyFlow.asStateFlow()
    
    private val _requestErrorChannel = Channel<String>()
    val requestErrorChannel = _requestErrorChannel.receiveAsFlow()
    
    private val _inputErrorFlow = MutableStateFlow<String?>(null)
    val inputErrorFlow = _inputErrorFlow.asStateFlow()
    
    private var _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    
    fun checkBin(number: String) {
        
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            val response = remoteRepository.binRequestApi.getBinInfo(number)
            when (response.code()) {
                200 -> {
                    _replyFlow.value = response.body()
                }
                429 -> {
                    _replyFlow.value = emptyReply
                    _requestErrorChannel.send("Limit reached. Wait 1 minute please")
                }
                else -> {
                    _replyFlow.value = emptyReply
                    _requestErrorChannel.send("Unknown BIN")
                    
                }
            }
            _isLoading.value = false
        }

//        viewModelScope.launch(Dispatchers.IO) {
//            kotlin.runCatching {
//                _isLoading.value = true
//                remoteRepository.binRequestApi.getBinInfo(number)
//            }.fold(
//                onSuccess = { _replyFlow.value = it.body()
//                    if (it.code() == 404)
//                            Log.d(TAG, it.toString())},
//                onFailure = { _requestErrorChannel.send(it.co) }
//            )
//            _isLoading.value = false
//        }
    }
    
    /**
     * Проверка на количество введенных цифр. Если цифр меньше 4х - сервер ничего не возвращает,
     * поэтому я запретил отправлять такие запросы.
     * */
    fun validateInput(input: CharSequence): Boolean {
        val isValid = input.length >= 4
        if (isValid) _inputErrorFlow.value = null
        else {
            if (input.isEmpty()) _inputErrorFlow.value = null
            else _inputErrorFlow.value = "Input at least 4 numbers"
        }
        return isValid
    }
}