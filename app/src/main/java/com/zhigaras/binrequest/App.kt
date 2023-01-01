package com.zhigaras.binrequest

import android.app.Application
import androidx.room.Room
import com.zhigaras.binrequest.database.AppDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application() {
    
    lateinit var db: AppDatabase
    
    override fun onCreate() {
        super.onCreate()
        
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "search_request_history_db"
        ).build()
    }
}