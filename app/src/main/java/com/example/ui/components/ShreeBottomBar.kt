package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.LocalizedStrings
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary

@Composable
fun ShreeBottomBar(
    currentRoute: String,
    currentLanguage: AppLanguage,
    isAdmin: Boolean,
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.navigationBars)
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF221E18), Color(0xFF14120F))
                )
            )
            .border(
                width = 1.dp,
                brush = Brush.horizontalGradient(
                    colors = listOf(GoldDark, GoldPrimary, GoldDark)
                ),
                shape = androidx.compose.ui.graphics.RectangleShape
            ),
        containerColor = Color.Transparent,
        tonalElevation = 8.dp
    ) {
        // Home Tab
        NavigationBarItem(
            selected = currentRoute == "home",
            onClick = { onNavigate("home") },
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home"
                )
            },
            label = {
                Text(
                    text = LocalizedStrings.get("markets", currentLanguage),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.Black,
                selectedTextColor = GoldPrimary,
                indicatorColor = GoldPrimary,
                unselectedIconColor = GoldDark,
                unselectedTextColor = Color.Gray
            )
        )

        // Bet History Tab
        NavigationBarItem(
            selected = currentRoute == "history",
            onClick = { onNavigate("history") },
            icon = {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = "History"
                )
            },
            label = {
                Text(
                    text = LocalizedStrings.get("betting_history", currentLanguage),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.Black,
                selectedTextColor = GoldPrimary,
                indicatorColor = GoldPrimary,
                unselectedIconColor = GoldDark,
                unselectedTextColor = Color.Gray
            )
        )

        // Wallet Tab
        NavigationBarItem(
            selected = currentRoute == "wallet",
            onClick = { onNavigate("wallet") },
            icon = {
                Icon(
                    imageVector = Icons.Default.AccountBalanceWallet,
                    contentDescription = "Wallet"
                )
            },
            label = {
                Text(
                    text = LocalizedStrings.get("wallet_balance", currentLanguage),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.Black,
                selectedTextColor = GoldPrimary,
                indicatorColor = GoldPrimary,
                unselectedIconColor = GoldDark,
                unselectedTextColor = Color.Gray
            )
        )

        // Analytics Tab
        NavigationBarItem(
            selected = currentRoute == "analytics",
            onClick = { onNavigate("analytics") },
            icon = {
                Icon(
                    imageVector = Icons.Default.Analytics,
                    contentDescription = "Analytics"
                )
            },
            label = {
                Text(
                    text = LocalizedStrings.get("analytics", currentLanguage),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.Black,
                selectedTextColor = GoldPrimary,
                indicatorColor = GoldPrimary,
                unselectedIconColor = GoldDark,
                unselectedTextColor = Color.Gray
            )
        )

        // Admin Panel Tab (Only visible when Admin Mode is unlocked)
        if (isAdmin) {
            NavigationBarItem(
                selected = currentRoute == "admin",
                onClick = { onNavigate("admin") },
                icon = {
                    Icon(
                        imageVector = Icons.Default.AdminPanelSettings,
                        contentDescription = "Admin"
                    )
                },
                label = {
                    Text(
                        text = LocalizedStrings.get("admin_panel", currentLanguage),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Black,
                    selectedTextColor = GoldPrimary,
                    indicatorColor = GoldPrimary,
                    unselectedIconColor = GoldDark,
                    unselectedTextColor = Color.Gray
                )
            )
        }
    }
}
