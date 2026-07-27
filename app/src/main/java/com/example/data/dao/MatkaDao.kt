package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.entity.BetEntity
import com.example.data.entity.MarketEntity
import com.example.data.entity.NotificationEntity
import com.example.data.entity.PromoCodeEntity
import com.example.data.entity.TransactionEntity
import com.example.data.entity.UserWalletEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MatkaDao {

    // User & Wallet
    @Query("SELECT * FROM user_wallet WHERE id = 1")
    fun getUserWalletFlow(): Flow<UserWalletEntity?>

    @Query("SELECT * FROM user_wallet WHERE id = 1")
    suspend fun getUserWallet(): UserWalletEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateWallet(wallet: UserWalletEntity)

    @Query("UPDATE user_wallet SET balance = balance + :amount WHERE id = 1")
    suspend fun creditWallet(amount: Double)

    @Query("UPDATE user_wallet SET balance = balance - :amount WHERE id = 1")
    suspend fun debitWallet(amount: Double)

    @Query("UPDATE user_wallet SET selectedLanguageCode = :langCode WHERE id = 1")
    suspend fun updateLanguage(langCode: String)

    @Query("UPDATE user_wallet SET isAdmin = :isAdmin WHERE id = 1")
    suspend fun updateAdminMode(isAdmin: Boolean)

    // Markets
    @Query("SELECT * FROM markets ORDER BY sortOrder ASC, id ASC")
    fun getAllMarketsFlow(): Flow<List<MarketEntity>>

    @Query("SELECT * FROM markets WHERE id = :id")
    suspend fun getMarketById(id: Long): MarketEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMarkets(markets: List<MarketEntity>)

    @Update
    suspend fun updateMarket(market: MarketEntity)

    @Query("UPDATE markets SET openPana = :openPana, openDigit = :openDigit, closeDigit = :closeDigit, closePana = :closePana WHERE id = :id")
    suspend fun updateMarketResult(id: Long, openPana: String, openDigit: String, closeDigit: String, closePana: String)

    // Bets
    @Query("SELECT * FROM bets ORDER BY timestamp DESC")
    fun getAllBetsFlow(): Flow<List<BetEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBet(bet: BetEntity)

    @Update
    suspend fun updateBet(bet: BetEntity)

    // Transactions
    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    fun getAllTransactionsFlow(): Flow<List<TransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Query("UPDATE transactions SET status = :status WHERE id = :txId")
    suspend fun updateTransactionStatus(txId: Long, status: String)

    // Promo Codes
    @Query("SELECT * FROM promo_codes WHERE code = :code LIMIT 1")
    suspend fun getPromoCode(code: String): PromoCodeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPromoCode(promoCode: PromoCodeEntity)

    @Query("UPDATE promo_codes SET isUsed = 1 WHERE code = :code")
    suspend fun markPromoUsed(code: String)

    @Query("SELECT * FROM promo_codes")
    fun getAllPromoCodesFlow(): Flow<List<PromoCodeEntity>>

    // Notifications
    @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
    fun getAllNotificationsFlow(): Flow<List<NotificationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity)

    @Query("UPDATE notifications SET isRead = 1")
    suspend fun markAllNotificationsRead()
}
