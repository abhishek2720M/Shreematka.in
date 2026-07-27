package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.BetEntity
import com.example.data.entity.MarketEntity
import com.example.data.entity.TransactionEntity
import com.example.data.model.AppLanguage
import com.example.data.model.PaymentSettings
import com.example.data.model.LocalizedStrings
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.AccentRed
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary

@Composable
fun AdminPanelScreen(
    markets: List<MarketEntity>,
    transactions: List<TransactionEntity>,
    bets: List<BetEntity>,
    currentLanguage: AppLanguage,
    paymentSettings: PaymentSettings = PaymentSettings(),
    onApproveTx: (txId: Long, isDeposit: Boolean, amount: Double) -> Unit,
    onRejectTx: (txId: Long, isDeposit: Boolean, amount: Double) -> Unit,
    onUpdateMarketResult: (marketId: Long, openPana: String, openDigit: String, closeDigit: String, closePana: String) -> Unit,
    onDeclareWinner: (bet: BetEntity) -> Unit,
    onSavePaymentSettings: (PaymentSettings) -> Unit = {}
) {
    var selectedAdminTab by remember { mutableStateOf(4) } // 4: WEB PORTAL SITE by default (0: Deposits, 1: Withdrawals, 2: Results, 3: Winners, 4: Web Portal, 5: Payment Config)

    val pendingDeposits = transactions.filter { it.type == "DEPOSIT" && it.status == "PENDING" }
    val pendingWithdrawals = transactions.filter { it.type == "WITHDRAWAL" && it.status == "PENDING" }
    val pendingBets = bets.filter { it.status == "PENDING" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF101010))
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Default.AdminPanelSettings, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = "SHREE MATKA ADMIN PANEL",
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp,
                    color = GoldPrimary
                )
                Text(
                    text = "Real-time user, market & payment details management",
                    fontSize = 11.sp,
                    color = AccentGreen
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        TabRow(
            selectedTabIndex = selectedAdminTab,
            containerColor = DarkSurface,
            contentColor = GoldPrimary,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedAdminTab]),
                    color = GoldPrimary
                )
            }
        ) {
            Tab(selected = selectedAdminTab == 0, onClick = { selectedAdminTab = 0 }, text = { Text("DEPOSITS (${pendingDeposits.size})", fontSize = 9.sp, fontWeight = FontWeight.Bold) })
            Tab(selected = selectedAdminTab == 1, onClick = { selectedAdminTab = 1 }, text = { Text("WITHDRAWS (${pendingWithdrawals.size})", fontSize = 9.sp, fontWeight = FontWeight.Bold) })
            Tab(selected = selectedAdminTab == 2, onClick = { selectedAdminTab = 2 }, text = { Text("RESULTS", fontSize = 9.sp, fontWeight = FontWeight.Bold) })
            Tab(selected = selectedAdminTab == 3, onClick = { selectedAdminTab = 3 }, text = { Text("WINNERS", fontSize = 9.sp, fontWeight = FontWeight.Bold) })
            Tab(selected = selectedAdminTab == 4, onClick = { selectedAdminTab = 4 }, text = { Text("WEB PORTAL", fontSize = 9.sp, fontWeight = FontWeight.Bold) })
            Tab(selected = selectedAdminTab == 5, onClick = { selectedAdminTab = 5 }, text = { Text("PAYMENT CONFIG", fontSize = 9.sp, fontWeight = FontWeight.Bold) })
        }

        Spacer(modifier = Modifier.height(12.dp))

        when (selectedAdminTab) {
            0 -> AdminDepositsList(pendingDeposits, onApproveTx, onRejectTx)
            1 -> AdminWithdrawalsList(pendingWithdrawals, onApproveTx, onRejectTx)
            2 -> AdminDeclareResultsList(markets, onUpdateMarketResult)
            3 -> AdminBetsWinnerList(pendingBets, onDeclareWinner)
            4 -> AdminWebSitePortal()
            5 -> AdminPaymentSettingsContent(paymentSettings, onSavePaymentSettings)
        }
    }
}

@Composable
fun AdminDepositsList(
    deposits: List<TransactionEntity>,
    onApprove: (Long, Boolean, Double) -> Unit,
    onReject: (Long, Boolean, Double) -> Unit
) {
    if (deposits.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No pending deposit requests", color = Color.Gray)
        }
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(deposits) { tx ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .border(1.dp, GoldDark, RoundedCornerShape(12.dp)),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Deposit Request #${tx.id}", fontWeight = FontWeight.Bold, color = GoldLight)
                            Text("₹${tx.amount.toInt()}", fontWeight = FontWeight.Black, color = AccentGreen, fontSize = 16.sp)
                        }

                        Text("Gateway: ${tx.upiOrBankDetails}", fontSize = 12.sp, color = Color.White)
                        Text("UTR / Ref ID: ${tx.referenceNo}", fontSize = 12.sp, color = GoldPrimary, fontWeight = FontWeight.Bold)

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            Button(
                                onClick = { onReject(tx.id, true, tx.amount) },
                                colors = ButtonDefaults.buttonColors(containerColor = AccentRed),
                                modifier = Modifier.height(36.dp),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("REJECT", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Button(
                                onClick = { onApprove(tx.id, true, tx.amount) },
                                colors = ButtonDefaults.buttonColors(containerColor = AccentGreen, contentColor = Color.Black),
                                modifier = Modifier.height(36.dp),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("APPROVE", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminWithdrawalsList(
    withdrawals: List<TransactionEntity>,
    onApprove: (Long, Boolean, Double) -> Unit,
    onReject: (Long, Boolean, Double) -> Unit
) {
    if (withdrawals.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No pending withdrawal requests", color = Color.Gray)
        }
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(withdrawals) { tx ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .border(1.dp, GoldDark, RoundedCornerShape(12.dp)),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Withdrawal Request #${tx.id}", fontWeight = FontWeight.Bold, color = GoldLight)
                            Text("₹${tx.amount.toInt()}", fontWeight = FontWeight.Black, color = AccentRed, fontSize = 16.sp)
                        }

                        Text("UPI / Account: ${tx.upiOrBankDetails}", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            Button(
                                onClick = { onReject(tx.id, false, tx.amount) },
                                colors = ButtonDefaults.buttonColors(containerColor = AccentRed),
                                modifier = Modifier.height(36.dp),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("REJECT", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Button(
                                onClick = { onApprove(tx.id, false, tx.amount) },
                                colors = ButtonDefaults.buttonColors(containerColor = AccentGreen, contentColor = Color.Black),
                                modifier = Modifier.height(36.dp),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("APPROVE", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminDeclareResultsList(
    markets: List<MarketEntity>,
    onUpdateResult: (Long, String, String, String, String) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(markets) { market ->
            var openPana by remember { mutableStateOf(market.openPana) }
            var openDigit by remember { mutableStateOf(market.openDigit) }
            var closeDigit by remember { mutableStateOf(market.closeDigit) }
            var closePana by remember { mutableStateOf(market.closePana) }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .border(1.dp, DarkCardBorder, RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = DarkSurface)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(market.name, fontWeight = FontWeight.Black, color = GoldPrimary, fontSize = 16.sp)
                    Text("Current Result: ${market.getFullResult()}", fontSize = 12.sp, color = GoldLight)

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        OutlinedTextField(
                            value = openPana,
                            onValueChange = { openPana = it },
                            label = { Text("Open Pana", fontSize = 10.sp) },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        OutlinedTextField(
                            value = openDigit,
                            onValueChange = { openDigit = it },
                            label = { Text("Open Dig", fontSize = 10.sp) },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        OutlinedTextField(
                            value = closeDigit,
                            onValueChange = { closeDigit = it },
                            label = { Text("Close Dig", fontSize = 10.sp) },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        OutlinedTextField(
                            value = closePana,
                            onValueChange = { closePana = it },
                            label = { Text("Close Pana", fontSize = 10.sp) },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { onUpdateResult(market.id, openPana, openDigit, closeDigit, closePana) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(38.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = Color.Black)
                    ) {
                        Text("PUBLISH LIVE RESULT", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun AdminBetsWinnerList(
    pendingBets: List<BetEntity>,
    onDeclareWinner: (BetEntity) -> Unit
) {
    if (pendingBets.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No pending bet slips", color = Color.Gray)
        }
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(pendingBets) { bet ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .border(1.dp, DarkCardBorder, RoundedCornerShape(12.dp)),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(bet.marketName, fontWeight = FontWeight.Bold, color = GoldPrimary)
                            Text("${bet.gameType}: ${bet.digitsChosen}", fontSize = 13.sp, color = Color.White)
                            Text("Points: ₹${bet.points.toInt()} -> Win: ₹${bet.potentialPayout.toInt()}", fontSize = 12.sp, color = AccentGreen)
                        }

                        Button(
                            onClick = { onDeclareWinner(bet) },
                            colors = ButtonDefaults.buttonColors(containerColor = AccentGreen, contentColor = Color.Black),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("MARK WINNER", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminWebSitePortal() {
    var webUrl by remember { mutableStateOf("https://admin.shreematka.com/live-control") }
    var copiedMsg by remember { mutableStateOf(false) }
    var showWebLoginDialog by remember { mutableStateOf(false) }
    var siteIdInput by remember { mutableStateOf("") }
    var sitePassInput by remember { mutableStateOf("") }
    var siteLoginError by remember { mutableStateOf<String?>(null) }
    var isSiteLoggedIn by remember { mutableStateOf(false) }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .border(1.dp, GoldDark, RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = DarkSurface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Language, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("WEB SITE ADMIN PORTAL", fontWeight = FontWeight.Bold, color = GoldPrimary, fontSize = 16.sp)
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "You can access the admin panel via Web URL from any Mobile, Laptop, or PC web browser.",
                        fontSize = 12.sp,
                        color = Color.LightGray
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = webUrl,
                        onValueChange = { webUrl = it },
                        label = { Text("Web Admin Site URL") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GoldPrimary,
                            unfocusedBorderColor = GoldDark,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = { copiedMsg = true },
                            colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = Color.Black),
                            modifier = Modifier.weight(1f).height(40.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (copiedMsg) "URL COPIED!" else "COPY WEB LINK", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                siteIdInput = ""
                                sitePassInput = ""
                                siteLoginError = null
                                showWebLoginDialog = true
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = AccentGreen, contentColor = Color.Black),
                            modifier = Modifier.weight(1f).height(40.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(imageVector = Icons.Default.OpenInNew, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("OPEN SITE", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Web Dashboard Simulator Preview Box
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, DarkCardBorder, RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Black, RoundedCornerShape(6.dp))
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.size(10.dp).background(AccentRed, CircleShape))
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(modifier = Modifier.size(10.dp).background(GoldPrimary, CircleShape))
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(modifier = Modifier.size(10.dp).background(AccentGreen, CircleShape))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(webUrl, fontSize = 11.sp, color = Color.Gray)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (isSiteLoggedIn) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = AccentGreen, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("WELCOME ADMIN Abhi272005", fontWeight = FontWeight.Bold, color = GoldPrimary, fontSize = 14.sp)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("💻 WEB SITE ADMIN PANEL: LOGGED IN & LIVE", fontWeight = FontWeight.Bold, color = AccentGreen, fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("• Security Token: SSL Encrypted Session Active", fontSize = 11.sp, color = Color.White)
                        Text("• Full Control: Market Rates, Winners, Deposits, Withdrawals", fontSize = 11.sp, color = Color.White)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { isSiteLoggedIn = false },
                            colors = ButtonDefaults.buttonColors(containerColor = AccentRed, contentColor = Color.White),
                            modifier = Modifier.height(32.dp),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text("LOGOUT WEB SESSION", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Text("💻 LIVE WEB PANEL STATUS: PROTECTED", fontWeight = FontWeight.Bold, color = GoldPrimary, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("• Web Server API: Connected (v2.4)", fontSize = 11.sp, color = Color.White)
                        Text("• Security Protocol: HTTPS / SSL 256-bit Encrypted", fontSize = 11.sp, color = Color.White)
                        Text("• Access Restriction: ID: Abhi272005 & Password Required", fontSize = 11.sp, color = GoldLight)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                siteIdInput = ""
                                sitePassInput = ""
                                siteLoginError = null
                                showWebLoginDialog = true
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = Color.Black),
                            modifier = Modifier.height(36.dp),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("LOGIN TO WEB SITE", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    if (showWebLoginDialog) {
        AlertDialog(
            onDismissRequest = { showWebLoginDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Language, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("🌐 Web Site Admin Login", fontWeight = FontWeight.Bold, color = GoldPrimary, fontSize = 16.sp)
                }
            },
            text = {
                Column {
                    Text(
                        text = "Enter Web Admin Credentials to open $webUrl",
                        fontSize = 12.sp,
                        color = Color.LightGray
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = siteIdInput,
                        onValueChange = {
                            siteIdInput = it
                            siteLoginError = null
                        },
                        label = { Text("Admin ID") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GoldPrimary,
                            unfocusedBorderColor = GoldDark,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = sitePassInput,
                        onValueChange = {
                            sitePassInput = it
                            siteLoginError = null
                        },
                        label = { Text("Admin Password") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GoldPrimary,
                            unfocusedBorderColor = GoldDark,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                    siteLoginError?.let { err ->
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = err, color = Color.Red, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (siteIdInput.trim() == "Abhi272005" && sitePassInput == "Abhishek272005@") {
                            showWebLoginDialog = false
                            isSiteLoggedIn = true
                        } else {
                            siteLoginError = "❌ Wrong ID or Password! ID: Abhi272005 required."
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = Color.Black)
                ) {
                    Text("OPEN SITE", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                Button(
                    onClick = { showWebLoginDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray, contentColor = Color.White)
                ) {
                    Text("CANCEL")
                }
            }
        )
    }
}

@Composable
fun AdminPaymentSettingsContent(
    initialSettings: PaymentSettings,
    onSave: (PaymentSettings) -> Unit
) {
    var upiId by remember { mutableStateOf(initialSettings.upiId) }
    var phonePeNumber by remember { mutableStateOf(initialSettings.phonePeNumber) }
    var gPayNumber by remember { mutableStateOf(initialSettings.gPayNumber) }
    var paytmNumber by remember { mutableStateOf(initialSettings.paytmNumber) }
    var bankAccountNo by remember { mutableStateOf(initialSettings.bankAccountNo) }
    var bankIfsc by remember { mutableStateOf(initialSettings.bankIfsc) }
    var bankHolderName by remember { mutableStateOf(initialSettings.bankHolderName) }
    var whatsappSupportNumber by remember { mutableStateOf(initialSettings.whatsappSupportNumber) }
    var minDeposit by remember { mutableStateOf(initialSettings.minDeposit.toString()) }
    var minWithdraw by remember { mutableStateOf(initialSettings.minWithdraw.toString()) }

    var isGPayEnabled by remember { mutableStateOf(initialSettings.isGPayEnabled) }
    var isPhonePeEnabled by remember { mutableStateOf(initialSettings.isPhonePeEnabled) }
    var isPaytmEnabled by remember { mutableStateOf(initialSettings.isPaytmEnabled) }
    var isBhimQrEnabled by remember { mutableStateOf(initialSettings.isBhimQrEnabled) }
    var isBankTransferEnabled by remember { mutableStateOf(initialSettings.isBankTransferEnabled) }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .border(1.dp, GoldPrimary, RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = DarkSurface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "⚡ APP PAYMENT DETAILS & GATEWAY CONTROL",
                        fontWeight = FontWeight.Black,
                        color = GoldPrimary,
                        fontSize = 15.sp
                    )
                    Text(
                        text = "Yahan se aap app ke andar dikhne wale payment options ko kabhi bhi change, add ya remove/hide kar sakte hain.",
                        fontSize = 11.sp,
                        color = Color.LightGray
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text("1. ENABLE / DISABLE PAYMENT METHODS", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(8.dp))

                    // Gateway Toggles
                    PaymentToggleRow("Google Pay UPI", isGPayEnabled) { isGPayEnabled = it }
                    PaymentToggleRow("PhonePe UPI", isPhonePeEnabled) { isPhonePeEnabled = it }
                    PaymentToggleRow("Paytm UPI", isPaytmEnabled) { isPaytmEnabled = it }
                    PaymentToggleRow("BHIM QR Code", isBhimQrEnabled) { isBhimQrEnabled = it }
                    PaymentToggleRow("Direct Bank Transfer", isBankTransferEnabled) { isBankTransferEnabled = it }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("2. EDIT UPI & BANK DETAILS", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = upiId,
                        onValueChange = { upiId = it },
                        label = { Text("Primary UPI ID") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GoldPrimary,
                            unfocusedBorderColor = GoldDark,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = gPayNumber,
                            onValueChange = { gPayNumber = it },
                            label = { Text("GPay Number") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = GoldPrimary, unfocusedBorderColor = GoldDark, focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedTextField(
                            value = phonePeNumber,
                            onValueChange = { phonePeNumber = it },
                            label = { Text("PhonePe Number") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = GoldPrimary, unfocusedBorderColor = GoldDark, focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = paytmNumber,
                        onValueChange = { paytmNumber = it },
                        label = { Text("Paytm Number") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = GoldPrimary, unfocusedBorderColor = GoldDark, focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("BANK TRANSFER DETAILS", fontWeight = FontWeight.Bold, color = GoldLight, fontSize = 11.sp)
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = bankHolderName,
                        onValueChange = { bankHolderName = it },
                        label = { Text("Account Holder Name") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = GoldPrimary, unfocusedBorderColor = GoldDark, focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = bankAccountNo,
                            onValueChange = { bankAccountNo = it },
                            label = { Text("Account Number") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = GoldPrimary, unfocusedBorderColor = GoldDark, focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedTextField(
                            value = bankIfsc,
                            onValueChange = { bankIfsc = it },
                            label = { Text("Bank IFSC Code") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = GoldPrimary, unfocusedBorderColor = GoldDark, focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("3. MINIMUM LIMITS (₹)", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = minDeposit,
                            onValueChange = { minDeposit = it },
                            label = { Text("Min Deposit (₹)") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = GoldPrimary, unfocusedBorderColor = GoldDark, focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedTextField(
                            value = minWithdraw,
                            onValueChange = { minWithdraw = it },
                            label = { Text("Min Withdrawal (₹)") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = GoldPrimary, unfocusedBorderColor = GoldDark, focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("4. WHATSAPP CUSTOMER SUPPORT NUMBER", fontWeight = FontWeight.Bold, color = Color(0xFF25D366), fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = whatsappSupportNumber,
                        onValueChange = { whatsappSupportNumber = it },
                        label = { Text("WhatsApp Support No. (e.g. 919876543210)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF25D366),
                            unfocusedBorderColor = GoldDark,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            val updated = PaymentSettings(
                                upiId = upiId,
                                phonePeNumber = phonePeNumber,
                                gPayNumber = gPayNumber,
                                paytmNumber = paytmNumber,
                                bankAccountNo = bankAccountNo,
                                bankIfsc = bankIfsc,
                                bankHolderName = bankHolderName,
                                minDeposit = minDeposit.toIntOrNull() ?: 100,
                                minWithdraw = minWithdraw.toIntOrNull() ?: 300,
                                isGPayEnabled = isGPayEnabled,
                                isPhonePeEnabled = isPhonePeEnabled,
                                isPaytmEnabled = isPaytmEnabled,
                                isBhimQrEnabled = isBhimQrEnabled,
                                isBankTransferEnabled = isBankTransferEnabled,
                                whatsappSupportNumber = whatsappSupportNumber
                            )
                            onSave(updated)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = Color.Black),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("SAVE & PUBLISH PAYMENT DETAILS", fontWeight = FontWeight.Black, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun PaymentToggleRow(title: String, isEnabled: Boolean, onToggle: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, fontSize = 12.sp, color = if (isEnabled) Color.White else Color.Gray)
        Switch(
            checked = isEnabled,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.Black,
                checkedTrackColor = GoldPrimary,
                uncheckedThumbColor = Color.Gray,
                uncheckedTrackColor = DarkSurface
            )
        )
    }
}
