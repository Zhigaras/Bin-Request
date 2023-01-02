package com.zhigaras.binrequest.repository

import androidx.datastore.preferences.core.Preferences
import com.zhigaras.binrequest.model.BinReplyModel
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val remoteRepository: RemoteRepository,
    private val localeRepository: LocaleRepository
) {
    
    suspend fun checkBin(number: String) : Response<BinReplyModel> {
        return remoteRepository.binRequestApi.getBinInfo(number)
    }
    
    suspend fun addToPrefs(item: String) {
        localeRepository.addToPrefs(item)
    }
    
    suspend fun clearPrefs() {
        localeRepository.clearPrefs()
    }
    
    fun getAllFromPrefs() : Flow<Preferences> {
        return localeRepository.getAllFromPrefs()
    }
}