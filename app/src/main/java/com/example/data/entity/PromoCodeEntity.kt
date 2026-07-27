package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "promo_codes")
data class PromoCodeEntity(
    @PrimaryKey val code: String,
    val bonusAmount: Double,
    val isUsed: Boolean = false,
    val maxUses: Int = 100,
    val description: String = "Bonus Credit"
)
