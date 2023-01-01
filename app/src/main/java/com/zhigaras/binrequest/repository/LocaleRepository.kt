package com.zhigaras.binrequest.repository

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOf
import okhttp3.internal.notify
import javax.inject.Inject

const val SEARCH_HISTORY_PREFS = "searchHistoryPrefs"

class LocaleRepository @Inject constructor() {
    
    private lateinit var searchHistoryPrefs: SharedPreferences
    
    fun initSharedPrefs(context: Context) {
        searchHistoryPrefs = context.getSharedPreferences(SEARCH_HISTORY_PREFS, MODE_PRIVATE)
    }
    
    fun addToPrefs(item: String) {
        searchHistoryPrefs.edit().putString(item, item).apply()
    }
    
    fun clearPrefs() {
        searchHistoryPrefs.edit().clear().apply()
    }
    
    fun getAllFromPrefs(): Flow<List<String>> {
        return flowOf(searchHistoryPrefs.all.map { (k,_) -> k }.sorted())
    }
    
}