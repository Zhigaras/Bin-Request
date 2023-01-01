package com.zhigaras.binrequest.repository

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import com.zhigaras.binrequest.presentation.TAG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOf
import okhttp3.internal.notify
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

const val SEARCH_HISTORY_PREFS = "searchHistoryPrefs"

class LocaleRepository @Inject constructor() {
    
    private lateinit var searchHistoryPrefs: SharedPreferences
    
    fun initSharedPrefs(context: Context) {
        searchHistoryPrefs = context.getSharedPreferences(SEARCH_HISTORY_PREFS, MODE_PRIVATE)
    }
    
    suspend fun addToPrefs(item: String) {
        if (searchHistoryPrefs.contains(item))
            searchHistoryPrefs.edit().remove(item).apply()
        searchHistoryPrefs.edit().putString(item, System.currentTimeMillis().toString()).apply()
    }
    
    suspend fun clearPrefs() {
        searchHistoryPrefs.edit().clear().apply()
    }
    
    fun getAllFromPrefs(): Flow<List<String>> {
        val allValues = searchHistoryPrefs.all.mapValues { (_, v) -> v.toString().toLongOrNull() }
        val sortedAllValues = allValues.toList().sortedBy { (_,v) -> v }
        return flowOf(sortedAllValues.map { (k, _) -> k.toString() }.reversed())
    }
    
}