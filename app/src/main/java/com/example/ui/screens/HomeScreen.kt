package com.example.ui.screens

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.entity.MarketEntity
import com.example.data.model.AppLanguage
import com.example.data.model.LocalizedStrings
import com.example.data.model.PaymentSettings
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.AccentRed
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary

@Composable
fun HomeScreen(
    markets: List<MarketEntity>,
    currentLanguage: AppLanguage,
    paymentSettings: PaymentSettings = PaymentSettings(),
    onMarketSelected: (MarketEntity) -> Unit,
    onDepositClick: () -> Unit,
    onWithdrawClick: () -> Unit,
    onReferClick: () -> Unit,
    onPromoClick: () -> Unit,
    onGameRatesClick: () -> Unit,
    onWebsiteClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val appShareUrl = "https://ais-pre-ownr5x7ppxujb6airryeet-281781359999.asia-southeast1.run.app"
    val shareText = "🏆 Join SHREE MATKA - India's No.1 Matka Play App!\n" +
            "Direct App Register & Play Link:\n$appShareUrl\n\n" +
            "🎁 Use Referral Code: SHREE99 to get ₹100 Welcome Bonus!"

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF101010))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        // Hero Banner Card
        item {
            HeroBannerCard(currentLanguage = currentLanguage)
            Spacer(modifier = Modifier.height(10.dp))
        }

        // WhatsApp Customer Support Banner
        item {
            val waNumber = paymentSettings.whatsappSupportNumber.ifEmpty { "919876543210" }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFF25D366), RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF13251A))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("💬 WHATSAPP CUSTOMER SUPPORT", fontWeight = FontWeight.Bold, color = Color(0xFF25D366), fontSize = 11.sp)
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("Need help or deposit support? Chat directly with Admin!", fontSize = 10.sp, color = Color.LightGray)
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    Button(
                        onClick = {
                            try {
                                val url = "https://api.whatsapp.com/send?phone=$waNumber&text=Hello%20Shree%20Matka%20Admin,%20I%20need%20support."
                                val intent = Intent(Intent.ACTION_VIEW, android.net.Uri.parse(url))
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(context, "WhatsApp Support: $waNumber", Toast.LENGTH_LONG).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366), contentColor = Color.White),
                        modifier = Modifier.height(34.dp),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text("CHAT 💬", fontSize = 10.sp, fontWeight = FontWeight.Black)
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        // Official Landing Website Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, GoldPrimary, RoundedCornerShape(12.dp))
                    .clickable { onWebsiteClick() },
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1610))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("🌐 OFFICIAL WEBSITE & PORTAL", fontWeight = FontWeight.Bold, color = GoldPrimary, fontSize = 12.sp)
                        Text("www.shreematkaplay.com (Live Landing Page)", fontSize = 10.sp, color = GoldLight)
                    }
                    Button(
                        onClick = { onWebsiteClick() },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = Color.Black),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("VIEW SITE 🌐", fontWeight = FontWeight.Bold, fontSize = 10.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        // Share App & Register Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, GoldPrimary, RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = DarkSurface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.PersonAdd, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("SHARE APP & REGISTER PLAYERS", fontWeight = FontWeight.Bold, color = GoldPrimary, fontSize = 11.sp)
                        }
                        Text("Share this app link with friends to let them register & play!", fontSize = 10.sp, color = Color.LightGray)
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    Button(
                        onClick = {
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, shareText)
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(sendIntent, "Share App Register Link"))
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = Color.Black),
                        modifier = Modifier.height(34.dp),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("SHARE LINK", fontSize = 10.sp, fontWeight = FontWeight.Black)
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Quick Actions Row Grid
        item {
            QuickActionsGrid(
                currentLanguage = currentLanguage,
                onDepositClick = onDepositClick,
                onWithdrawClick = onWithdrawClick,
                onReferClick = onReferClick,
                onPromoClick = onPromoClick,
                onGameRatesClick = onGameRatesClick
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Live Results Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(AccentGreen)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = LocalizedStrings.get("live_results", currentLanguage),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        color = GoldPrimary
                    )
                }

                Text(
                    text = "Fast Update ⚡",
                    fontSize = 11.sp,
                    color = AccentGreen,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Market Cards List
        items(markets) { market ->
            MarketCardItem(
                market = market,
                currentLanguage = currentLanguage,
                onPlayClick = { onMarketSelected(market) }
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun HeroBannerCard(currentLanguage: AppLanguage) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .border(1.dp, GoldPrimary, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.img_hero_banner_1785089677413),
                contentDescription = "Banner",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.85f),
                                Color.Black.copy(alpha = 0.4f)
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "🇮🇳 " + LocalizedStrings.get("tagline", currentLanguage),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    color = GoldPrimary
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Play Kalyan, Rajdhani, Main Bazaar & Get Instant Payouts!",
                    fontSize = 12.sp,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(AccentGreen)
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "LIVE",
                            color = Color.Black,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "100% Minimum Deposit ₹100 • 24/7 Support",
                        color = GoldLight,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun QuickActionsGrid(
    currentLanguage: AppLanguage,
    onDepositClick: () -> Unit,
    onWithdrawClick: () -> Unit,
    onReferClick: () -> Unit,
    onPromoClick: () -> Unit,
    onGameRatesClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        QuickActionButton(
            label = LocalizedStrings.get("add_money", currentLanguage),
            icon = Icons.Default.AddCard,
            iconTint = AccentGreen,
            onClick = onDepositClick,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(6.dp))

        QuickActionButton(
            label = LocalizedStrings.get("withdraw", currentLanguage),
            icon = Icons.Default.AccountBalanceWallet,
            iconTint = GoldPrimary,
            onClick = onWithdrawClick,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(6.dp))

        QuickActionButton(
            label = LocalizedStrings.get("game_rates", currentLanguage),
            icon = Icons.Default.Stars,
            iconTint = GoldLight,
            onClick = onGameRatesClick,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(6.dp))

        QuickActionButton(
            label = LocalizedStrings.get("refer_earn", currentLanguage),
            icon = Icons.Default.Share,
            iconTint = Color(0xFF64B5F6),
            onClick = onReferClick,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(6.dp))

        QuickActionButton(
            label = LocalizedStrings.get("promo_code", currentLanguage),
            icon = Icons.Default.CardGiftcard,
            iconTint = Color(0xFFFFB74D),
            onClick = onPromoClick,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun QuickActionButton(
    label: String,
    icon: ImageVector,
    iconTint: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, DarkCardBorder, RoundedCornerShape(12.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = DarkSurface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconTint,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 1
            )
        }
    }
}

@Composable
fun MarketCardItem(
    market: MarketEntity,
    currentLanguage: AppLanguage,
    onPlayClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, if (market.isOpen) GoldDark else DarkCardBorder, RoundedCornerShape(14.dp)),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Market Title
                Text(
                    text = market.name,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    color = GoldPrimary
                )

                // Status Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (market.isOpen) AccentGreen.copy(alpha = 0.2f) else AccentRed.copy(alpha = 0.2f))
                        .border(
                            width = 1.dp,
                            color = if (market.isOpen) AccentGreen else AccentRed,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = if (market.isOpen) LocalizedStrings.get("open", currentLanguage) else LocalizedStrings.get("close", currentLanguage),
                        color = if (market.isOpen) AccentGreen else AccentRed,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Result Display Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF26221B))
                    .border(1.dp, GoldDark, RoundedCornerShape(10.dp))
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = market.getFullResult(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = GoldLight,
                    letterSpacing = 2.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Open: ${market.openTime}",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "Close: ${market.closeTime}",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }

                Button(
                    onClick = onPlayClick,
                    enabled = market.isOpen,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldPrimary,
                        contentColor = Color.Black,
                        disabledContainerColor = Color.DarkGray,
                        disabledContentColor = Color.Gray
                    ),
                    modifier = Modifier.height(38.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = LocalizedStrings.get("play_now", currentLanguage),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
