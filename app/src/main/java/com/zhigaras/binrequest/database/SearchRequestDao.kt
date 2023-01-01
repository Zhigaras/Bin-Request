package com.zhigaras.binrequest.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchRequestDao {
    
    @Insert
    fun addRequest(item: String) {
    
    }
    @Query("DELETE FROM search_request")
    fun clearAll() {
    
    }
    
    @Query("SELECT * FROM search_request ORDER BY requestDate DESC")
    fun getAll(): Flow<List<SearchRequestEntity>>
}