package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.TransactionEntity
import com.example.data.model.AppLanguage
import com.example.data.model.LocalizedStrings
import com.example.data.model.PaymentSettings
import com.example.ui.theme.AccentBlue
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.AccentRed
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun WalletScreen(
    balance: Double,
    transactions: List<TransactionEntity>,
    currentLanguage: AppLanguage,
    paymentSettings: PaymentSettings = PaymentSettings(),
    onDepositSubmit: (amount: String, utr: String, gateway: String) -> Unit,
    onWithdrawSubmit: (amount: String, upiId: String) -> Unit,
    onApplyPromo: (code: String) -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) } // 0: Deposit, 1: Withdraw, 2: History
    var promoCodeInput by remember { mutableStateOf("") }

    // Deposit fields
    var depositAmount by remember { mutableStateOf(paymentSettings.minDeposit.toString()) }
    var utrInput by remember { mutableStateOf("") }
    var selectedGateway by remember { mutableStateOf("GPay UPI") }

    // Withdraw fields
    var withdrawAmount by remember { mutableStateOf(paymentSettings.minWithdraw.toString()) }
    var upiIdInput by remember { mutableStateOf("user@upi") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF101010))
            .padding(12.dp)
    ) {
        // Balance Header Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, GoldPrimary, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = LocalizedStrings.get("wallet_balance", currentLanguage),
                    fontSize = 12.sp,
                    color = Color.LightGray,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "₹${balance.toInt()}",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    color = GoldPrimary
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = AccentGreen,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "100% Encrypted Payment Gateway",
                        color = AccentGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Promo Code Redeem Bar
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, DarkCardBorder, RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(containerColor = DarkSurface)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CardGiftcard,
                    contentDescription = null,
                    tint = GoldPrimary,
                    modifier = Modifier.padding(start = 8.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = promoCodeInput,
                    onValueChange = { promoCodeInput = it },
                    placeholder = { Text("Promo Code (e.g. SHREE100)", color = Color.Gray, fontSize = 12.sp) },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GoldDark,
                        unfocusedBorderColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
                Button(
                    onClick = {
                        onApplyPromo(promoCodeInput)
                        promoCodeInput = ""
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = Color.Black),
                    modifier = Modifier.height(40.dp)
                ) {
                    Text("APPLY", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Section Tabs: Deposit / Withdraw / Statement
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = DarkSurface,
            contentColor = GoldPrimary,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = GoldPrimary
                )
            }
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("DEPOSIT", fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("WITHDRAW", fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = { Text("STATEMENT", fontWeight = FontWeight.Bold) }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        when (selectedTab) {
            0 -> DepositTabContent(
                amount = depositAmount,
                onAmountChange = { depositAmount = it },
                utr = utrInput,
                onUtrChange = { utrInput = it },
                selectedGateway = selectedGateway,
                onGatewaySelect = { selectedGateway = it },
                paymentSettings = paymentSettings,
                onSubmit = {
                    onDepositSubmit(depositAmount, utrInput, selectedGateway)
                    utrInput = ""
                }
            )

            1 -> WithdrawTabContent(
                amount = withdrawAmount,
                onAmountChange = { withdrawAmount = it },
                upiId = upiIdInput,
                onUpiIdChange = { upiIdInput = it },
                onSubmit = { onWithdrawSubmit(withdrawAmount, upiIdInput) }
            )

            2 -> TransactionHistoryContent(transactions = transactions)
        }
    }
}

@Composable
fun DepositTabContent(
    amount: String,
    onAmountChange: (String) -> Unit,
    utr: String,
    onUtrChange: (String) -> Unit,
    selectedGateway: String,
    onGatewaySelect: (String) -> Unit,
    paymentSettings: PaymentSettings,
    onSubmit: () -> Unit
) {
    val quickAmounts = listOf("100", "200", "500", "1000", "2000", "5000")
    
    val activeGateways = mutableListOf<String>()
    if (paymentSettings.isGPayEnabled) activeGateways.add("GPay UPI")
    if (paymentSettings.isPhonePeEnabled) activeGateways.add("PhonePe")
    if (paymentSettings.isPaytmEnabled) activeGateways.add("Paytm UPI")
    if (paymentSettings.isBhimQrEnabled) activeGateways.add("BHIM QR")
    if (paymentSettings.isBankTransferEnabled) activeGateways.add("Bank Transfer")
    
    if (activeGateways.isEmpty()) {
        activeGateways.add("BHIM QR")
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Text("Select Active Payment App", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                activeGateways.forEach { g ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(2.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (selectedGateway == g) GoldPrimary else DarkSurface)
                            .border(1.dp, GoldDark, RoundedCornerShape(8.dp))
                            .clickable { onGatewaySelect(g) }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = g,
                            color = if (selectedGateway == g) Color.Black else Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Deposit Amount Input
            Text("Deposit Amount (Min ₹${paymentSettings.minDeposit})", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = amount,
                onValueChange = onAmountChange,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GoldPrimary,
                    unfocusedBorderColor = DarkCardBorder,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = DarkSurface,
                    unfocusedContainerColor = DarkSurface
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Quick Chips
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                quickAmounts.forEach { q ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (amount == q) GoldPrimary else DarkSurface)
                            .clickable { onAmountChange(q) }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text("₹$q", color = if (amount == q) Color.Black else GoldLight, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Live Payment Details Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, GoldDark, RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = DarkSurface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.QrCode, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(36.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        when (selectedGateway) {
                            "GPay UPI" -> {
                                Text("GPay Number: ${paymentSettings.gPayNumber}", fontWeight = FontWeight.Bold, color = GoldLight, fontSize = 13.sp)
                                Text("UPI ID: ${paymentSettings.upiId}", color = Color.LightGray, fontSize = 11.sp)
                            }
                            "PhonePe" -> {
                                Text("PhonePe Number: ${paymentSettings.phonePeNumber}", fontWeight = FontWeight.Bold, color = GoldLight, fontSize = 13.sp)
                                Text("UPI ID: ${paymentSettings.upiId}", color = Color.LightGray, fontSize = 11.sp)
                            }
                            "Paytm UPI" -> {
                                Text("Paytm Number: ${paymentSettings.paytmNumber}", fontWeight = FontWeight.Bold, color = GoldLight, fontSize = 13.sp)
                                Text("UPI ID: ${paymentSettings.upiId}", color = Color.LightGray, fontSize = 11.sp)
                            }
                            "Bank Transfer" -> {
                                Text("Acc: ${paymentSettings.bankAccountNo}", fontWeight = FontWeight.Bold, color = GoldLight, fontSize = 13.sp)
                                Text("IFSC: ${paymentSettings.bankIfsc} | Holder: ${paymentSettings.bankHolderName}", color = Color.LightGray, fontSize = 11.sp)
                            }
                            else -> {
                                Text("UPI ID: ${paymentSettings.upiId}", fontWeight = FontWeight.Bold, color = GoldLight, fontSize = 13.sp)
                                Text("Pay via Google Pay / PhonePe / Paytm / BHIM QR", color = Color.LightGray, fontSize = 11.sp)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // UTR Number Input
            Text("Enter Transaction Ref / UTR Number", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = utr,
                onValueChange = onUtrChange,
                placeholder = { Text("e.g. 329182391029", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GoldPrimary,
                    unfocusedBorderColor = DarkCardBorder,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = DarkSurface,
                    unfocusedContainerColor = DarkSurface
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onSubmit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = Color.Black)
            ) {
                Icon(imageVector = Icons.Default.AddCard, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("SUBMIT DEPOSIT REQUEST", fontWeight = FontWeight.Black, fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun WithdrawTabContent(
    amount: String,
    onAmountChange: (String) -> Unit,
    upiId: String,
    onUpiIdChange: (String) -> Unit,
    onSubmit: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Bonus Rule Note Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF231F17), RoundedCornerShape(10.dp))
                .border(1.dp, GoldDark, RoundedCornerShape(10.dp))
                .padding(10.dp)
        ) {
            Text(
                text = "📌 Note: Welcome Bonus (₹50) is Playable Bonus for placing bets on Matka games. All game winnings can be withdrawn directly!",
                fontWeight = FontWeight.Medium,
                color = GoldLight,
                fontSize = 11.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text("Withdrawal Amount (Min ₹300)", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = amount,
            onValueChange = onAmountChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GoldPrimary,
                unfocusedBorderColor = DarkCardBorder,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = DarkSurface,
                unfocusedContainerColor = DarkSurface
            )
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text("Your UPI ID / PhonePe / Paytm / Bank Account", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = upiId,
            onValueChange = onUpiIdChange,
            placeholder = { Text("e.g. 9876543210@paytm", color = Color.Gray) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GoldPrimary,
                unfocusedBorderColor = DarkCardBorder,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = DarkSurface,
                unfocusedContainerColor = DarkSurface
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onSubmit,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = Color.Black)
        ) {
            Icon(imageVector = Icons.Default.AccountBalanceWallet, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("REQUEST WITHDRAWAL", fontWeight = FontWeight.Black, fontSize = 14.sp)
        }
    }
}

@Composable
fun TransactionHistoryContent(transactions: List<TransactionEntity>) {
    val formatter = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())

    if (transactions.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No transactions found", color = Color.Gray)
        }
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(transactions) { tx ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .border(1.dp, DarkCardBorder, RoundedCornerShape(10.dp)),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when (tx.type) {
                                            "DEPOSIT", "WIN_PAYOUT", "PROMO_BONUS", "REFERRAL_BONUS" -> AccentGreen.copy(alpha = 0.2f)
                                            else -> AccentRed.copy(alpha = 0.2f)
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (tx.type == "DEPOSIT" || tx.type == "WIN_PAYOUT" || tx.type == "PROMO_BONUS") Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                                    contentDescription = null,
                                    tint = if (tx.type == "DEPOSIT" || tx.type == "WIN_PAYOUT" || tx.type == "PROMO_BONUS") AccentGreen else AccentRed,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Column {
                                Text(
                                    text = tx.type.replace("_", " "),
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = formatter.format(Date(tx.timestamp)),
                                    color = Color.Gray,
                                    fontSize = 10.sp
                                )
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "${if (tx.type == "DEPOSIT" || tx.type == "WIN_PAYOUT" || tx.type == "PROMO_BONUS" || tx.type == "REFERRAL_BONUS") "+" else "-"}₹${tx.amount.toInt()}",
                                fontWeight = FontWeight.Black,
                                color = if (tx.type == "DEPOSIT" || tx.type == "WIN_PAYOUT" || tx.type == "PROMO_BONUS" || tx.type == "REFERRAL_BONUS") AccentGreen else AccentRed,
                                fontSize = 15.sp
                            )

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(
                                        when (tx.status) {
                                            "APPROVED", "SUCCESS" -> AccentGreen.copy(alpha = 0.2f)
                                            "PENDING" -> AccentBlue.copy(alpha = 0.2f)
                                            else -> AccentRed.copy(alpha = 0.2f)
                                        }
                                    )
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = tx.status,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = when (tx.status) {
                                        "APPROVED", "SUCCESS" -> AccentGreen
                                        "PENDING" -> AccentBlue
                                        else -> AccentRed
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
