package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "markets")
data class MarketEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val openTime: String,
    val closeTime: String,
    val openPana: String = "***",
    val openDigit: String = "*",
    val closeDigit: String = "*",
    val closePana: String = "***",
    val isOpen: Boolean = true,
    val chartUrl: String = "",
    val sortOrder: Int = 0
) {
    fun getFullResult(): String {
        return "$openPana-$openDigit$closeDigit-$closePana"
    }
}
