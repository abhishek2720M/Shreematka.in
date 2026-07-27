package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val message: String,
    val isRead: Boolean = false,
    val category: String = "ALERT", // RESULT, WALLET, PROMO, ALERT
    val timestamp: Long = System.currentTimeMillis()
)
