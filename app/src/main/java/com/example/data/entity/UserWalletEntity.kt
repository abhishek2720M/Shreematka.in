package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_wallet")
data class UserWalletEntity(
    @PrimaryKey val id: Int = 1,
    val userName: String = "Rajan Sharma",
    val phoneNumber: String = "9876543210",
    val password: String = "123456",
    val balance: Double = 1000.0,
    val referralCode: String = "SHREE99",
    val referredCount: Int = 3,
    val totalBonusEarned: Double = 300.0,
    val selectedLanguageCode: String = "hi",
    val isAdmin: Boolean = false,
    val isLoggedIn: Boolean = true
)

