package com.zhigaras.binrequest.repository

import android.content.Context
import com.zhigaras.binrequest.model.BinReplyModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import retrofit2.Response
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val remoteRepository: RemoteRepository,
    private val localeRepository: LocaleRepository
) {
    
    suspend fun checkBin(number: String) : Response<BinReplyModel> {
        return remoteRepository.binRequestApi.getBinInfo(number)
    }
    
    fun initSharedPrefs(context: Context) {
        localeRepository.initSharedPrefs(context)
    }
    
    suspend fun addToPrefs(item: String) {
        localeRepository.addToPrefs(item)
    }
    
    suspend fun clearPrefs() {
        localeRepository.clearPrefs()
    }
    fun getAllFromPrefs() : Flow<List<String>> {
        return localeRepository.getAllFromPrefs()
    }
}