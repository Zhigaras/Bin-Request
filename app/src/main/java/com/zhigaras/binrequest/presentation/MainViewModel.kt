package com.zhigaras.binrequest.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zhigaras.binrequest.model.BinReplyModel
import com.zhigaras.binrequest.repository.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository
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
    
    val requestHistoryFlow = mainRepository.getAllFromPrefs()
    
    fun checkBin(number: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            val response = mainRepository.checkBin(number)
            when (response.code()) {
                200 -> { /** Success */
                    _replyFlow.value = response.body()
                }
                429 -> { /** Exceeding the limit of 10 requests per minute */
                    _replyFlow.value = emptyReply
                    _requestErrorChannel.send("Limit reached. Wait 1 minute please")
                }
                else -> { /** Empty reply, unknown BIN */
                    _replyFlow.value = emptyReply
                    _requestErrorChannel.send("Unknown BIN")
                }
            }
            _isLoading.value = false
        }
    }
    
    /**
     * Check for the number of entered digits. If there are less than 4 digits
     * the server does not return anything so I forbade sending such requests.
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
    
    fun addRequestToPrefs(number: String) {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.addToPrefs(number)
        }
    }
    
    fun clearPrefs() {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.clearPrefs()
        }
    }
}