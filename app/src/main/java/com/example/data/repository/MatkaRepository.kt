package com.example.data.repository

import com.example.data.dao.MatkaDao
import com.example.data.entity.BetEntity
import com.example.data.entity.MarketEntity
import com.example.data.entity.NotificationEntity
import com.example.data.entity.PromoCodeEntity
import com.example.data.entity.TransactionEntity
import com.example.data.entity.UserWalletEntity
import kotlinx.coroutines.flow.Flow

class MatkaRepository(private val dao: MatkaDao) {

    val walletFlow: Flow<UserWalletEntity?> = dao.getUserWalletFlow()
    val marketsFlow: Flow<List<MarketEntity>> = dao.getAllMarketsFlow()
    val betsFlow: Flow<List<BetEntity>> = dao.getAllBetsFlow()
    val transactionsFlow: Flow<List<TransactionEntity>> = dao.getAllTransactionsFlow()
    val promoCodesFlow: Flow<List<PromoCodeEntity>> = dao.getAllPromoCodesFlow()
    val notificationsFlow: Flow<List<NotificationEntity>> = dao.getAllNotificationsFlow()

    suspend fun seedInitialData() {
        // Seed Wallet if missing
        if (dao.getUserWallet() == null) {
            dao.insertOrUpdateWallet(
                UserWalletEntity(
                    id = 1,
                    userName = "Rajan Sharma",
                    phoneNumber = "+91 9876543210",
                    balance = 1500.0,
                    referralCode = "SHREE789",
                    referredCount = 4,
                    totalBonusEarned = 400.0,
                    selectedLanguageCode = "hi",
                    isAdmin = false
                )
            )
        }

        // Seed Markets
        val initialMarkets = listOf(
            MarketEntity(id = 1, name = "KALYAN MORNING", openTime = "11:00 AM", closeTime = "12:02 PM", openPana = "138", openDigit = "2", closeDigit = "9", closePana = "450", isOpen = true, sortOrder = 1),
            MarketEntity(id = 2, name = "TIME BAZAR", openTime = "01:00 PM", closeTime = "02:00 PM", openPana = "240", openDigit = "6", closeDigit = "3", closePana = "148", isOpen = true, sortOrder = 2),
            MarketEntity(id = 3, name = "MILAN DAY", openTime = "03:15 PM", closeTime = "05:15 PM", openPana = "146", openDigit = "1", closeDigit = "5", closePana = "230", isOpen = true, sortOrder = 3),
            MarketEntity(id = 4, name = "KALYAN", openTime = "04:10 PM", closeTime = "06:10 PM", openPana = "258", openDigit = "5", closeDigit = "8", closePana = "378", isOpen = true, sortOrder = 4),
            MarketEntity(id = 5, name = "SRIDEVI NIGHT", openTime = "07:00 PM", closeTime = "08:00 PM", openPana = "120", openDigit = "3", closeDigit = "7", closePana = "467", isOpen = true, sortOrder = 5),
            MarketEntity(id = 6, name = "RAJDHANI NIGHT", openTime = "09:30 PM", closeTime = "11:45 PM", openPana = "135", openDigit = "9", closeDigit = "4", closePana = "248", isOpen = true, sortOrder = 6),
            MarketEntity(id = 7, name = "MAIN BAZAAR", openTime = "09:40 PM", closeTime = "12:05 AM", openPana = "350", openDigit = "8", closeDigit = "1", closePana = "236", isOpen = true, sortOrder = 7),
            MarketEntity(id = 8, name = "DISAWAR", openTime = "05:00 AM", closeTime = "05:15 AM", openPana = "***", openDigit = "7", closeDigit = "2", closePana = "***", isOpen = false, sortOrder = 8)
        )
        dao.insertMarkets(initialMarkets)

        // Seed Promo Codes
        val initialPromos = listOf(
            PromoCodeEntity(code = "SHREE100", bonusAmount = 100.0, description = "Welcome Free Bonus ₹100"),
            PromoCodeEntity(code = "WELCOME500", bonusAmount = 500.0, description = "First Deposit Match ₹500"),
            PromoCodeEntity(code = "MEGA20", bonusAmount = 200.0, description = "Festival Special Bonus ₹200")
        )
        for (promo in initialPromos) {
            if (dao.getPromoCode(promo.code) == null) {
                dao.insertPromoCode(promo)
            }
        }

        // Seed Initial Notifications
        dao.insertNotification(
            NotificationEntity(
                title = "🎉 Welcome to SHREE MATKA!",
                message = "India's No. 1 Matka platform! Get ₹100 free bonus with code SHREE100.",
                category = "PROMO"
            )
        )
        dao.insertNotification(
            NotificationEntity(
                title = "🔥 Kalyan Result Declared!",
                message = "Kalyan Open: 258-5 | Close: 8-378. Check winning slips in Bet History.",
                category = "RESULT"
            )
        )
    }

    suspend fun placeBet(
        marketId: Long,
        marketName: String,
        gameType: String,
        digitsChosen: String,
        points: Double,
        rateMultiplier: Double
    ): Boolean {
        val wallet = dao.getUserWallet() ?: return false
        if (wallet.balance < points) return false

        // Debit wallet
        dao.debitWallet(points)

        // Record bet
        val potentialPayout = points * rateMultiplier
        val bet = BetEntity(
            marketId = marketId,
            marketName = marketName,
            gameType = gameType,
            digitsChosen = digitsChosen,
            points = points,
            potentialPayout = potentialPayout,
            status = "PENDING"
        )
        dao.insertBet(bet)

        // Record transaction
        dao.insertTransaction(
            TransactionEntity(
                type = "BET_PLACED",
                amount = points,
                status = "SUCCESS",
                note = "Bet on $marketName ($gameType: $digitsChosen)"
            )
        )

        // Push notification
        dao.insertNotification(
            NotificationEntity(
                title = "🎯 Bet Placed Successfully",
                message = "₹${points.toInt()} bet on $marketName - $gameType ($digitsChosen). Potential Win: ₹${potentialPayout.toInt()}",
                category = "ALERT"
            )
        )
        return true
    }

    suspend fun requestDeposit(amount: Double, upiTxId: String, gatewayName: String) {
        dao.insertTransaction(
            TransactionEntity(
                type = "DEPOSIT",
                amount = amount,
                status = "PENDING",
                upiOrBankDetails = gatewayName,
                referenceNo = upiTxId,
                note = "Deposit via $gatewayName (UTR: $upiTxId)"
            )
        )
        dao.insertNotification(
            NotificationEntity(
                title = "⏳ Deposit Request Submitted",
                message = "Your deposit of ₹${amount.toInt()} (UTR: $upiTxId) is under verification.",
                category = "WALLET"
            )
        )
    }

    suspend fun requestWithdrawal(amount: Double, upiId: String): Boolean {
        val wallet = dao.getUserWallet() ?: return false
        if (wallet.balance < amount) return false

        dao.debitWallet(amount)
        dao.insertTransaction(
            TransactionEntity(
                type = "WITHDRAWAL",
                amount = amount,
                status = "PENDING",
                upiOrBankDetails = upiId,
                note = "Withdrawal request to $upiId"
            )
        )
        dao.insertNotification(
            NotificationEntity(
                title = "💸 Withdrawal Request Created",
                message = "Request for ₹${amount.toInt()} to $upiId submitted. Processing time: ~15 mins.",
                category = "WALLET"
            )
        )
        return true
    }

    suspend fun applyPromoCode(code: String): Pair<Boolean, String> {
        val cleanCode = code.trim().uppercase()
        val promo = dao.getPromoCode(cleanCode) ?: return Pair(false, "Invalid Promo Code!")
        if (promo.isUsed) return Pair(false, "This Promo Code has already been used!")

        dao.creditWallet(promo.bonusAmount)
        dao.markPromoUsed(cleanCode)

        dao.insertTransaction(
            TransactionEntity(
                type = "PROMO_BONUS",
                amount = promo.bonusAmount,
                status = "SUCCESS",
                note = "Claimed promo code $cleanCode"
            )
        )
        dao.insertNotification(
            NotificationEntity(
                title = "🎁 Promo Bonus Credited!",
                message = "₹${promo.bonusAmount.toInt()} credited to your wallet via code $cleanCode.",
                category = "PROMO"
            )
        )
        return Pair(true, "Success! ₹${promo.bonusAmount.toInt()} bonus added to your wallet!")
    }

    suspend fun processReferralBonus(referredName: String) {
        val bonus = 100.0
        dao.creditWallet(bonus)
        val wallet = dao.getUserWallet()
        if (wallet != null) {
            dao.insertOrUpdateWallet(
                wallet.copy(
                    referredCount = wallet.referredCount + 1,
                    totalBonusEarned = wallet.totalBonusEarned + bonus
                )
            )
        }
        dao.insertTransaction(
            TransactionEntity(
                type = "REFERRAL_BONUS",
                amount = bonus,
                status = "SUCCESS",
                note = "Referral bonus for joining $referredName"
            )
        )
        dao.insertNotification(
            NotificationEntity(
                title = "👥 Referral Bonus Credited",
                message = "You earned ₹100 for referring $referredName!",
                category = "PROMO"
            )
        )
    }

    // Admin Actions
    suspend fun approveTransaction(txId: Long, isDeposit: Boolean, amount: Double) {
        dao.updateTransactionStatus(txId, "APPROVED")
        if (isDeposit) {
            dao.creditWallet(amount)
        }
        dao.insertNotification(
            NotificationEntity(
                title = "✅ Request Approved",
                message = if (isDeposit) "Your deposit of ₹${amount.toInt()} has been approved & added!" else "Your withdrawal of ₹${amount.toInt()} has been transferred!",
                category = "WALLET"
            )
        )
    }

    suspend fun rejectTransaction(txId: Long, isDeposit: Boolean, amount: Double) {
        dao.updateTransactionStatus(txId, "REJECTED")
        if (!isDeposit) {
            // Refund wallet if withdrawal rejected
            dao.creditWallet(amount)
        }
        dao.insertNotification(
            NotificationEntity(
                title = "❌ Request Rejected",
                message = if (isDeposit) "Deposit of ₹${amount.toInt()} failed verification." else "Withdrawal of ₹${amount.toInt()} rejected. Funds refunded to wallet.",
                category = "WALLET"
            )
        )
    }

    suspend fun updateMarketResult(marketId: Long, openPana: String, openDigit: String, closeDigit: String, closePana: String) {
        dao.updateMarketResult(marketId, openPana, openDigit, closeDigit, closePana)
        val market = dao.getMarketById(marketId)
        val marketName = market?.name ?: "Market"
        dao.insertNotification(
            NotificationEntity(
                title = "📢 Live Result Out: $marketName",
                message = "New Result: $openPana - $openDigit$closeDigit - $closePana",
                category = "RESULT"
            )
        )
    }

    suspend fun declareBetWinner(bet: BetEntity) {
        val updatedBet = bet.copy(status = "WON", winAmount = bet.potentialPayout)
        dao.updateBet(updatedBet)
        dao.creditWallet(bet.potentialPayout)
        dao.insertTransaction(
            TransactionEntity(
                type = "WIN_PAYOUT",
                amount = bet.potentialPayout,
                status = "SUCCESS",
                note = "Won bet on ${bet.marketName} (${bet.gameType})"
            )
        )
        dao.insertNotification(
            NotificationEntity(
                title = "🏆 CONGRATULATIONS! BIG WIN!",
                message = "You WON ₹${bet.potentialPayout.toInt()} on ${bet.marketName} (${bet.digitsChosen})!",
                category = "RESULT"
            )
        )
    }

    suspend fun updateLanguage(langCode: String) {
        dao.updateLanguage(langCode)
    }

    suspend fun setAdminMode(isAdmin: Boolean) {
        dao.updateAdminMode(isAdmin)
    }

    suspend fun registerPlayer(fullName: String, phone: String, pass: String, refCode: String): Pair<Boolean, String> {
        val randomSuffix = (100..999).random()
        val userRef = "SHREE$randomSuffix"
        val newUser = UserWalletEntity(
            id = 1,
            userName = fullName,
            phoneNumber = phone,
            password = pass,
            balance = 50.0, // ₹50 Registration Free Playable Bonus
            referralCode = userRef,
            referredCount = 0,
            totalBonusEarned = 50.0,
            selectedLanguageCode = "hi",
            isAdmin = false,
            isLoggedIn = true
        )
        dao.insertOrUpdateWallet(newUser)
        dao.insertTransaction(
            TransactionEntity(
                type = "WELCOME_BONUS",
                amount = 50.0,
                status = "SUCCESS",
                note = "Free ₹50 Welcome Playable Registration Bonus"
            )
        )
        dao.insertNotification(
            NotificationEntity(
                title = "🎁 Welcome ₹50 Playable Bonus!",
                message = "Congratulations $fullName! ₹50 Free Playable Welcome Bonus has been credited. Play games to win & withdraw cash!",
                category = "PROMO"
            )
        )
        return Pair(true, "Registration Successful! ₹50 Bonus Credited.")
    }

    suspend fun loginPlayer(phone: String, pass: String): Pair<Boolean, String> {
        val currentWallet = dao.getUserWallet()
        if (currentWallet != null) {
            dao.insertOrUpdateWallet(
                currentWallet.copy(
                    phoneNumber = phone,
                    password = pass,
                    isLoggedIn = true
                )
            )
            return Pair(true, "Welcome Back, ${currentWallet.userName}!")
        }
        return Pair(false, "Invalid credentials!")
    }

    suspend fun logoutPlayer() {
        val current = dao.getUserWallet()
        if (current != null) {
            dao.insertOrUpdateWallet(current.copy(isLoggedIn = false))
        }
    }
}
