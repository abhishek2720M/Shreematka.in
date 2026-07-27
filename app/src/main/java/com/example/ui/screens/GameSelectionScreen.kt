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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.data.entity.MarketEntity
import com.example.data.model.AppLanguage
import com.example.data.model.LocalizedStrings
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary

data class GameTypeOption(
    val nameKey: String,
    val title: String,
    val rateText: String,
    val multiplier: Double
)

@Composable
fun GameSelectionScreen(
    market: MarketEntity?,
    currentLanguage: AppLanguage,
    onBackClick: () -> Unit,
    onGameTypeSelected: (String) -> Unit
) {
    val gameTypes = listOf(
        GameTypeOption("single_digit", LocalizedStrings.get("single_digit", currentLanguage), "10 KA 95", 9.5),
        GameTypeOption("jodi_digit", LocalizedStrings.get("jodi_digit", currentLanguage), "10 KA 950", 95.0),
        GameTypeOption("single_pana", LocalizedStrings.get("single_pana", currentLanguage), "10 KA 1400", 140.0),
        GameTypeOption("double_pana", LocalizedStrings.get("double_pana", currentLanguage), "10 KA 2800", 280.0),
        GameTypeOption("triple_pana", LocalizedStrings.get("triple_pana", currentLanguage), "10 KA 6000", 600.0),
        GameTypeOption("half_sangam", LocalizedStrings.get("half_sangam", currentLanguage), "10 KA 10000", 1000.0),
        GameTypeOption("full_sangam", LocalizedStrings.get("full_sangam", currentLanguage), "10 KA 100000", 10000.0)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF101010))
            .padding(12.dp)
    ) {
        // Header Bar
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
                    text = market?.name ?: "MARKET",
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp,
                    color = GoldPrimary
                )
                Text(
                    text = LocalizedStrings.get("select_game", currentLanguage),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Market Result Banner
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, GoldDark, RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(containerColor = DarkSurface)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("LIVE RESULT", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                    Text(market?.getFullResult() ?: "***-**-***", fontSize = 18.sp, fontWeight = FontWeight.Black, color = GoldLight)
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(GoldPrimary)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text("OPEN NOW", color = Color.Black, fontWeight = FontWeight.ExtraBold, fontSize = 11.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Game Types List
        LazyColumn {
            items(gameTypes) { option ->
                GameTypeCardItem(
                    option = option,
                    onClick = { onGameTypeSelected(option.title) }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
fun GameTypeCardItem(
    option: GameTypeOption,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, DarkCardBorder, RoundedCornerShape(14.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF26221A))
                        .border(1.dp, GoldDark, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Casino,
                        contentDescription = null,
                        tint = GoldPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = option.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color.White
                    )
                    Text(
                        text = "Rate: ${option.rateText}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = GoldLight
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = GoldDark
            )
        }
    }
}
