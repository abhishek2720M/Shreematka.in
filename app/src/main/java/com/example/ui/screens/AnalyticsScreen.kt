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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.BetEntity
import com.example.data.model.AppLanguage
import com.example.data.model.LocalizedStrings
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.AccentRed
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary

@Composable
fun AnalyticsScreen(
    bets: List<BetEntity>,
    currentLanguage: AppLanguage
) {
    val totalBets = bets.size
    val totalWagered = bets.sumOf { it.points }
    val wonBets = bets.filter { it.status == "WON" }
    val totalWonAmount = wonBets.sumOf { it.winAmount }
    val winRate = if (totalBets > 0) (wonBets.size.toDouble() / totalBets * 100) else 0.0
    val netProfitLoss = totalWonAmount - totalWagered

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF101010))
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Default.Analytics, contentDescription = null, tint = GoldPrimary)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = LocalizedStrings.get("analytics", currentLanguage) + " Dashboard",
                fontWeight = FontWeight.Black,
                fontSize = 20.sp,
                color = GoldPrimary
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            // Net Profit / Loss Overview
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, if (netProfitLoss >= 0) AccentGreen else AccentRed, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = LocalizedStrings.get("profit_loss", currentLanguage),
                            fontSize = 12.sp,
                            color = Color.LightGray,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${if (netProfitLoss >= 0) "+" else ""}₹${netProfitLoss.toInt()}",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Black,
                            color = if (netProfitLoss >= 0) AccentGreen else AccentRed
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (netProfitLoss >= 0) "🔥 Excellent Profit Margin!" else "⚡ Keep playing with calculated bets!",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
            }

            // Key Performance Metrics Grid
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MetricCard(
                        title = LocalizedStrings.get("total_bets", currentLanguage),
                        value = totalBets.toString(),
                        subtitle = "₹${totalWagered.toInt()} Played",
                        icon = Icons.Default.ShowChart,
                        iconTint = GoldPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    MetricCard(
                        title = LocalizedStrings.get("total_wins", currentLanguage),
                        value = "₹${totalWonAmount.toInt()}",
                        subtitle = "${wonBets.size} Won Slips",
                        icon = Icons.Default.EmojiEvents,
                        iconTint = AccentGreen,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            // Win Rate Progress Bar
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, DarkCardBorder, RoundedCornerShape(14.dp)),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = LocalizedStrings.get("win_rate", currentLanguage),
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "%.1f%%".format(winRate),
                                fontWeight = FontWeight.Black,
                                color = GoldPrimary,
                                fontSize = 16.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        LinearProgressIndicator(
                            progress = { (winRate / 100.0).toFloat().coerceIn(0f, 1f) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                                .clip(RoundedCornerShape(5.dp)),
                            color = AccentGreen,
                            trackColor = Color(0xFF2B2822)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
            }

            // Popular Game Types Breakdown
            item {
                Text(
                    text = "Game Type Distribution",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 15.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                val types = listOf("Single Digit", "Jodi Digit", "Single Pana", "Double Pana", "Triple Pana")
                types.forEach { gameType ->
                    val count = bets.count { it.gameType == gameType }
                    val percent = if (totalBets > 0) (count.toDouble() / totalBets * 100) else 0.0

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1916))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(gameType, color = GoldLight, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text("$count bets (%.0f%%)".format(percent), color = Color.Gray, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MetricCard(
    title: String,
    value: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.border(1.dp, DarkCardBorder, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = DarkSurface)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text(title, fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
            Text(value, fontSize = 18.sp, fontWeight = FontWeight.Black, color = Color.White)
            Text(subtitle, fontSize = 10.sp, color = GoldLight)
        }
    }
}
