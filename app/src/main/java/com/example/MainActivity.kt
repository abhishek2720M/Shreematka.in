package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.entity.MarketEntity
import com.example.data.model.AppLanguage
import com.example.ui.components.AuthDialog
import com.example.ui.components.ShreeBottomBar
import com.example.ui.components.ShreeTopAppBar
import com.example.ui.screens.AdminPanelScreen
import com.example.ui.screens.AnalyticsScreen
import com.example.ui.screens.BetHistoryScreen
import com.example.ui.screens.GameSelectionScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.NotificationsScreen
import com.example.ui.screens.PlaceBetScreen
import com.example.ui.screens.ReferralScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.WalletScreen
import com.example.ui.screens.WebsitePreviewScreen
import com.example.ui.theme.ShreeMatkaTheme
import com.example.ui.viewmodel.MatkaViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: MatkaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ShreeMatkaTheme {
                ShreeMatkaApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun ShreeMatkaApp(viewModel: MatkaViewModel) {
    val wallet by viewModel.wallet.collectAsStateWithLifecycle()
    val markets by viewModel.markets.collectAsStateWithLifecycle()
    val bets by viewModel.bets.collectAsStateWithLifecycle()
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()
    val notifications by viewModel.notifications.collectAsStateWithLifecycle()
    val currentLanguage by viewModel.currentLanguage.collectAsStateWithLifecycle()
    val selectedMarket by viewModel.selectedMarketForBet.collectAsStateWithLifecycle()
    val selectedGameType by viewModel.selectedGameType.collectAsStateWithLifecycle()
    val paymentSettings by viewModel.paymentSettings.collectAsStateWithLifecycle()

    var currentRoute by remember { mutableStateOf("splash") }
    var showAuthDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val balance = wallet?.balance ?: 0.0
    val isAdmin = wallet?.isAdmin ?: false
    val unreadNotifs = notifications.count { !it.isRead }

    // Toast & Snackbar Listener
    LaunchedEffect(Unit) {
        viewModel.toastMessage.collect { msg ->
            scope.launch {
                snackbarHostState.showSnackbar(msg)
            }
        }
    }

    if (showAuthDialog) {
        AuthDialog(
            onDismiss = { showAuthDialog = false },
            onRegister = { name, phone, pass, refCode ->
                viewModel.registerPlayer(name, phone, pass, refCode) {
                    showAuthDialog = false
                    currentRoute = "home"
                }
            },
            onLogin = { phone, pass ->
                viewModel.loginPlayer(phone, pass) {
                    showAuthDialog = false
                    currentRoute = "home"
                }
            }
        )
    }

    Scaffold(
        topBar = {
            if (currentRoute != "splash") {
                ShreeTopAppBar(
                    balance = balance,
                    unreadNotificationsCount = unreadNotifs,
                    currentLanguage = currentLanguage,
                    isAdmin = isAdmin,
                    onLanguageSelected = { viewModel.setLanguage(it) },
                    onWalletClick = { currentRoute = "wallet" },
                    onNotificationsClick = { currentRoute = "notifications" },
                    onAdminToggle = { viewModel.toggleAdminMode(it) },
                    onAuthClick = { showAuthDialog = true },
                    onWebsiteClick = { currentRoute = "website_preview" }
                )
            }
        },
        bottomBar = {
            if (currentRoute != "splash" && currentRoute != "game_select" && currentRoute != "place_bet" && currentRoute != "referral" && currentRoute != "notifications" && currentRoute != "website_preview") {
                ShreeBottomBar(
                    currentRoute = currentRoute,
                    currentLanguage = currentLanguage,
                    isAdmin = isAdmin,
                    onNavigate = { route -> currentRoute = route }
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color(0xFF101010)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFF101010))
        ) {
            when (currentRoute) {
                "splash" -> SplashScreen(
                    currentLanguage = currentLanguage,
                    onStartClick = { currentRoute = "home" },
                    onRegisterClick = { showAuthDialog = true }
                )

                "home" -> HomeScreen(
                    markets = markets,
                    currentLanguage = currentLanguage,
                    paymentSettings = paymentSettings,
                    onMarketSelected = { market ->
                        viewModel.selectMarketForBet(market)
                        currentRoute = "game_select"
                    },
                    onDepositClick = { currentRoute = "wallet" },
                    onWithdrawClick = { currentRoute = "wallet" },
                    onReferClick = { currentRoute = "referral" },
                    onPromoClick = { currentRoute = "wallet" },
                    onGameRatesClick = { currentRoute = "game_select" },
                    onWebsiteClick = { currentRoute = "website_preview" }
                )

                "game_select" -> GameSelectionScreen(
                    market = selectedMarket,
                    currentLanguage = currentLanguage,
                    onBackClick = { currentRoute = "home" },
                    onGameTypeSelected = { type ->
                        viewModel.selectGameType(type)
                        currentRoute = "place_bet"
                    }
                )

                "place_bet" -> PlaceBetScreen(
                    market = selectedMarket,
                    gameType = selectedGameType,
                    multiplier = viewModel.getRateMultiplier(selectedGameType),
                    walletBalance = balance,
                    currentLanguage = currentLanguage,
                    onBackClick = { currentRoute = "game_select" },
                    onPlaceBetClick = { digits, points ->
                        viewModel.placeBet(digits, points) {
                            currentRoute = "history"
                        }
                    }
                )

                "wallet" -> WalletScreen(
                    balance = balance,
                    transactions = transactions,
                    currentLanguage = currentLanguage,
                    paymentSettings = paymentSettings,
                    onDepositSubmit = { amount, utr, gateway ->
                        viewModel.requestDeposit(amount, utr, gateway) {}
                    },
                    onWithdrawSubmit = { amount, upiId ->
                        viewModel.requestWithdrawal(amount, upiId) {}
                    },
                    onApplyPromo = { code ->
                        viewModel.applyPromoCode(code)
                    }
                )

                "history" -> BetHistoryScreen(
                    bets = bets,
                    currentLanguage = currentLanguage
                )

                "analytics" -> AnalyticsScreen(
                    bets = bets,
                    currentLanguage = currentLanguage
                )

                "admin" -> AdminPanelScreen(
                    markets = markets,
                    transactions = transactions,
                    bets = bets,
                    currentLanguage = currentLanguage,
                    paymentSettings = paymentSettings,
                    onApproveTx = { txId, isDeposit, amount ->
                        viewModel.adminApproveTx(txId, isDeposit, amount)
                    },
                    onRejectTx = { txId, isDeposit, amount ->
                        viewModel.adminRejectTx(txId, isDeposit, amount)
                    },
                    onUpdateMarketResult = { marketId, openPana, openDigit, closeDigit, closePana ->
                        viewModel.adminUpdateMarketResult(marketId, openPana, openDigit, closeDigit, closePana)
                    },
                    onDeclareWinner = { bet ->
                        viewModel.adminDeclareWinner(bet)
                    },
                    onSavePaymentSettings = { updatedSettings ->
                        viewModel.updatePaymentSettings(updatedSettings)
                    }
                )

                "referral" -> ReferralScreen(
                    userWallet = wallet,
                    currentLanguage = currentLanguage,
                    onBackClick = { currentRoute = "home" },
                    onSimulateReferral = { friendName ->
                        viewModel.simulateReferral(friendName)
                    }
                )

                "notifications" -> NotificationsScreen(
                    notifications = notifications,
                    currentLanguage = currentLanguage,
                    onBackClick = { currentRoute = "home" }
                )

                "website_preview" -> WebsitePreviewScreen(
                    onBackClick = { currentRoute = "home" }
                )
            }
        }
    }
}
