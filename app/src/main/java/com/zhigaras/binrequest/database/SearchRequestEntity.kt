package com.zhigaras.binrequest.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_request")
data class SearchRequestEntity(
    @PrimaryKey
    @ColumnInfo(name = "cardNumber")
    val cardNumber: String,
    @ColumnInfo(name = "requestDate")
    val requestDate: String
)