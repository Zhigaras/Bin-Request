package com.zhigaras.binrequest.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SearchRequestEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun searchRequestDao(): SearchRequestDao
}