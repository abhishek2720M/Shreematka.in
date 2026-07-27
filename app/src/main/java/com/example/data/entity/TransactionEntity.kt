package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: String, // DEPOSIT, WITHDRAWAL, BET_PLACED, WIN_PAYOUT, PROMO_BONUS, REFERRAL_BONUS
    val amount: Double,
    val status: String = "SUCCESS", // PENDING, APPROVED, REJECTED, SUCCESS
    val upiOrBankDetails: String = "",
    val referenceNo: String = "",
    val note: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
