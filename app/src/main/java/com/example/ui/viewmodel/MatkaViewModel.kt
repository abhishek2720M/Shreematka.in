package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.AppDatabase
import com.example.data.entity.BetEntity
import com.example.data.entity.MarketEntity
import com.example.data.entity.NotificationEntity
import com.example.data.entity.PromoCodeEntity
import com.example.data.entity.TransactionEntity
import com.example.data.entity.UserWalletEntity
import com.example.data.model.AppLanguage
import com.example.data.model.PaymentSettings
import com.example.data.repository.MatkaRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MatkaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MatkaRepository

    val wallet: StateFlow<UserWalletEntity?>
    val markets: StateFlow<List<MarketEntity>>
    val bets: StateFlow<List<BetEntity>>
    val transactions: StateFlow<List<TransactionEntity>>
    val notifications: StateFlow<List<NotificationEntity>>
    val promoCodes: StateFlow<List<PromoCodeEntity>>

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage: SharedFlow<String> = _toastMessage.asSharedFlow()

    private val _selectedMarketForBet = MutableStateFlow<MarketEntity?>(null)
    val selectedMarketForBet: StateFlow<MarketEntity?> = _selectedMarketForBet.asStateFlow()

    private val _selectedGameType = MutableStateFlow("Single Digit")
    val selectedGameType: StateFlow<String> = _selectedGameType.asStateFlow()

    private val _paymentSettings = MutableStateFlow(PaymentSettings())
    val paymentSettings: StateFlow<PaymentSettings> = _paymentSettings.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        repository = MatkaRepository(database.matkaDao())

        viewModelScope.launch {
            repository.seedInitialData()
        }

        wallet = repository.walletFlow.stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(5000), null
        )
        markets = repository.marketsFlow.stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
        )
        bets = repository.betsFlow.stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
        )
        transactions = repository.transactionsFlow.stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
        )
        notifications = repository.notificationsFlow.stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
        )
        promoCodes = repository.promoCodesFlow.stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
        )
    }

    val currentLanguage: StateFlow<AppLanguage> = wallet.map { user ->
        val code = user?.selectedLanguageCode ?: "en"
        AppLanguage.entries.find { it.code == code } ?: AppLanguage.ENGLISH
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppLanguage.ENGLISH)

    fun selectMarketForBet(market: MarketEntity?) {
        _selectedMarketForBet.value = market
    }

    fun selectGameType(gameType: String) {
        _selectedGameType.value = gameType
    }

    fun getRateMultiplier(gameType: String): Double {
        return when (gameType) {
            "Single Digit" -> 9.5
            "Jodi Digit" -> 95.0
            "Single Pana" -> 140.0
            "Double Pana" -> 280.0
            "Triple Pana" -> 600.0
            "Half Sangam" -> 1000.0
            "Full Sangam" -> 10000.0
            else -> 10.0
        }
    }

    fun placeBet(digits: String, pointsStr: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val points = pointsStr.toDoubleOrNull() ?: 0.0
            if (points <= 0) {
                _toastMessage.emit("Please enter valid points!")
                return@launch
            }
            if (digits.isBlank()) {
                _toastMessage.emit("Please enter digit or pana!")
                return@launch
            }
            val market = _selectedMarketForBet.value
            if (market == null) {
                _toastMessage.emit("Please select a market first!")
                return@launch
            }

            val gameType = _selectedGameType.value
            val multiplier = getRateMultiplier(gameType)

            val success = repository.placeBet(
                marketId = market.id,
                marketName = market.name,
                gameType = gameType,
                digitsChosen = digits,
                points = points,
                rateMultiplier = multiplier
            )

            if (success) {
                _toastMessage.emit("Bet placed successfully!")
                onSuccess()
            } else {
                _toastMessage.emit("Insufficient balance in wallet!")
            }
        }
    }

    fun requestDeposit(amountStr: String, utrId: String, gatewayName: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val amount = amountStr.toDoubleOrNull() ?: 0.0
            val minDep = _paymentSettings.value.minDeposit
            if (amount < minDep) {
                _toastMessage.emit("Minimum deposit amount is ₹$minDep")
                return@launch
            }
            if (utrId.isBlank()) {
                _toastMessage.emit("Please enter UTR or Transaction Ref ID!")
                return@launch
            }

            repository.requestDeposit(amount, utrId, gatewayName)
            _toastMessage.emit("Deposit request submitted! Approval in progress.")
            onSuccess()
        }
    }

    fun requestWithdrawal(amountStr: String, upiId: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val amount = amountStr.toDoubleOrNull() ?: 0.0
            val minWd = _paymentSettings.value.minWithdraw
            if (amount < minWd) {
                _toastMessage.emit("Minimum withdrawal amount is ₹$minWd")
                return@launch
            }
            if (upiId.isBlank()) {
                _toastMessage.emit("Please enter valid UPI ID / Bank Details")
                return@launch
            }

            val success = repository.requestWithdrawal(amount, upiId)
            if (success) {
                _toastMessage.emit("Withdrawal request created successfully!")
                onSuccess()
            } else {
                _toastMessage.emit("Insufficient wallet balance for withdrawal!")
            }
        }
    }

    fun updatePaymentSettings(newSettings: PaymentSettings) {
        viewModelScope.launch {
            _paymentSettings.value = newSettings
            _toastMessage.emit("✅ Payment Settings & Gateway Details Updated Successfully!")
        }
    }

    fun applyPromoCode(code: String) {
        viewModelScope.launch {
            if (code.isBlank()) {
                _toastMessage.emit("Please enter promo code")
                return@launch
            }
            val (success, message) = repository.applyPromoCode(code)
            _toastMessage.emit(message)
        }
    }

    fun simulateReferral(friendName: String) {
        viewModelScope.launch {
            if (friendName.isBlank()) {
                _toastMessage.emit("Enter friend's name")
                return@launch
            }
            repository.processReferralBonus(friendName)
            _toastMessage.emit("Referral successful! ₹100 bonus credited.")
        }
    }

    fun setLanguage(language: AppLanguage) {
        viewModelScope.launch {
            repository.updateLanguage(language.code)
            _toastMessage.emit("Language changed to ${language.displayName}")
        }
    }

    fun toggleAdminMode(isAdmin: Boolean) {
        viewModelScope.launch {
            repository.setAdminMode(isAdmin)
            val msg = if (isAdmin) "Admin Panel Activated" else "Switched to User Mode"
            _toastMessage.emit(msg)
        }
    }

    // Admin Panel Methods
    fun adminApproveTx(txId: Long, isDeposit: Boolean, amount: Double) {
        viewModelScope.launch {
            repository.approveTransaction(txId, isDeposit, amount)
            _toastMessage.emit("Transaction Approved!")
        }
    }

    fun adminRejectTx(txId: Long, isDeposit: Boolean, amount: Double) {
        viewModelScope.launch {
            repository.rejectTransaction(txId, isDeposit, amount)
            _toastMessage.emit("Transaction Rejected!")
        }
    }

    fun adminUpdateMarketResult(marketId: Long, openPana: String, openDigit: String, closeDigit: String, closePana: String) {
        viewModelScope.launch {
            repository.updateMarketResult(marketId, openPana, openDigit, closeDigit, closePana)
            _toastMessage.emit("Market Result Updated & Broadcasted!")
        }
    }

    fun adminDeclareWinner(bet: BetEntity) {
        viewModelScope.launch {
            repository.declareBetWinner(bet)
            _toastMessage.emit("Bet declared as Winner! Payout credited to user.")
        }
    }

    fun registerPlayer(fullName: String, phone: String, pass: String, refCode: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            if (fullName.isBlank()) {
                _toastMessage.emit("Please enter your Full Name!")
                return@launch
            }
            if (phone.length < 10) {
                _toastMessage.emit("Please enter a valid 10-digit mobile number!")
                return@launch
            }
            if (pass.length < 4) {
                _toastMessage.emit("Password must be at least 4 characters!")
                return@launch
            }

            val (success, msg) = repository.registerPlayer(fullName, phone, pass, refCode)
            _toastMessage.emit(msg)
            if (success) onSuccess()
        }
    }

    fun loginPlayer(phone: String, pass: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            if (phone.length < 10) {
                _toastMessage.emit("Please enter valid 10-digit mobile number!")
                return@launch
            }
            if (pass.isBlank()) {
                _toastMessage.emit("Please enter your password!")
                return@launch
            }

            val (success, msg) = repository.loginPlayer(phone, pass)
            _toastMessage.emit(msg)
            if (success) onSuccess()
        }
    }

    fun logoutPlayer() {
        viewModelScope.launch {
            repository.logoutPlayer()
            _toastMessage.emit("Logged Out Successfully!")
        }
    }
}
