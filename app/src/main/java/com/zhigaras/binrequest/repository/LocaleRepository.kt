package com.zhigaras.binrequest.repository

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocaleRepository @Inject constructor(
    application: Application
) {
    private val context = application.applicationContext
    
    private val Context.dataStore by preferencesDataStore(name = "requestHistoryDataStore")
    private val dataStore = context.dataStore
    
    suspend fun addToPrefs(item: String) {
        dataStore.edit { prefs ->
            if (prefs.contains(longPreferencesKey(item))) {
                prefs.remove(longPreferencesKey(item))
            }
            prefs[longPreferencesKey(item)] = System.currentTimeMillis()
        }
    }
    
    suspend fun clearPrefs() {
        dataStore.edit { prefs ->
            prefs.clear()
        }
    }
    
    fun getAllFromPrefs(): Flow<Preferences> {
        return dataStore.data
    }
}