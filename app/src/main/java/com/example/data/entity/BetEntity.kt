package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bets")
data class BetEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val marketId: Long,
    val marketName: String,
    val gameType: String, // Single Digit, Jodi, Single Pana, Double Pana, Triple Pana, Half Sangam, Full Sangam
    val digitsChosen: String, // e.g. "7", "48", "124"
    val points: Double,
    val potentialPayout: Double,
    val status: String = "PENDING", // PENDING, WON, LOST
    val winAmount: Double = 0.0,
    val timestamp: Long = System.currentTimeMillis()
)
