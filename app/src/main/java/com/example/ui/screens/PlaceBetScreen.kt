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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.MarketEntity
import com.example.data.model.AppLanguage
import com.example.data.model.LocalizedStrings
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary

@Composable
fun PlaceBetScreen(
    market: MarketEntity?,
    gameType: String,
    multiplier: Double,
    walletBalance: Double,
    currentLanguage: AppLanguage,
    onBackClick: () -> Unit,
    onPlaceBetClick: (digits: String, points: String) -> Unit
) {
    var digitsInput by remember { mutableStateOf("") }
    var pointsInput by remember { mutableStateOf("50") }

    val quickPoints = listOf("10", "50", "100", "200", "500", "1000")
    val sampleDigits = if (gameType == "Single Digit") listOf("0","1","2","3","4","5","6","7","8","9") else emptyList()

    val pointsVal = pointsInput.toDoubleOrNull() ?: 0.0
    val potentialPayout = pointsVal * multiplier

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF101010))
            .padding(14.dp)
    ) {
        // Top Header Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = GoldPrimary
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = "${market?.name ?: "Market"} - $gameType",
                    fontWeight = FontWeight.Black,
                    fontSize = 17.sp,
                    color = GoldPrimary
                )
                Text(
                    text = "Payout Multiplier: ${multiplier.toInt()}X",
                    fontSize = 12.sp,
                    color = AccentGreen,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Wallet Balance Indicator Card
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
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.MonetizationOn,
                        contentDescription = null,
                        tint = GoldPrimary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = LocalizedStrings.get("wallet_balance", currentLanguage),
                        color = Color.LightGray,
                        fontSize = 13.sp
                    )
                }

                Text(
                    text = "₹${walletBalance.toInt()}",
                    color = GoldLight,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Digit Entry Field
        Text(
            text = LocalizedStrings.get("enter_digit", currentLanguage) + " ($gameType)",
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = digitsInput,
            onValueChange = { digitsInput = it.take(8) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GoldPrimary,
                unfocusedBorderColor = DarkCardBorder,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = DarkSurface,
                unfocusedContainerColor = DarkSurface
            ),
            placeholder = { Text("e.g. 7 or 48 or 124", color = Color.Gray) },
            singleLine = true
        )

        // Single Digit Fast Selector Grid
        if (sampleDigits.isNotEmpty()) {
            Spacer(modifier = Modifier.height(10.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(5),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            ) {
                items(sampleDigits) { d ->
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .clip(CircleShape)
                            .background(if (digitsInput == d) GoldPrimary else DarkSurface)
                            .border(1.dp, GoldDark, CircleShape)
                            .clickable { digitsInput = d }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = d,
                            color = if (digitsInput == d) Color.Black else Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Points Entry Field
        Text(
            text = LocalizedStrings.get("enter_points", currentLanguage),
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = pointsInput,
            onValueChange = { pointsInput = it },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GoldPrimary,
                unfocusedBorderColor = DarkCardBorder,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = DarkSurface,
                unfocusedContainerColor = DarkSurface
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Quick Points Chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            quickPoints.forEach { q ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (pointsInput == q) GoldPrimary else DarkSurface)
                        .border(1.dp, GoldDark, RoundedCornerShape(8.dp))
                        .clickable { pointsInput = q }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "₹$q",
                        color = if (pointsInput == q) Color.Black else GoldLight,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Potential Win Calculation Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, AccentGreen, RoundedCornerShape(14.dp)),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1B231B))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("POTENTIAL WINNING", fontSize = 11.sp, color = AccentGreen, fontWeight = FontWeight.Bold)
                    Text("₹${potentialPayout.toInt()}", fontSize = 22.sp, fontWeight = FontWeight.Black, color = Color.White)
                }

                Text("${multiplier.toInt()}X PAYOUT", fontSize = 12.sp, color = GoldPrimary, fontWeight = FontWeight.ExtraBold)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Place Bet Button
        Button(
            onClick = { onPlaceBetClick(digitsInput, pointsInput) },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(26.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = GoldPrimary,
                contentColor = Color.Black
            )
        ) {
            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = LocalizedStrings.get("place_bet", currentLanguage),
                fontWeight = FontWeight.Black,
                fontSize = 16.sp
            )
        }
    }
}
