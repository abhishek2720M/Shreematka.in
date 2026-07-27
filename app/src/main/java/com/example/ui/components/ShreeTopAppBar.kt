package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Notifications
import com.example.data.model.AppLanguage
import com.example.data.model.LocalizedStrings
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary

@Composable
fun ShreeTopAppBar(
    balance: Double,
    unreadNotificationsCount: Int,
    currentLanguage: AppLanguage,
    isAdmin: Boolean,
    onLanguageSelected: (AppLanguage) -> Unit,
    onWalletClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onAdminToggle: (Boolean) -> Unit,
    onAuthClick: () -> Unit = {},
    onWebsiteClick: () -> Unit = {}
) {
    var showLangMenu by remember { mutableStateOf(false) }
    var showAdminLoginDialog by remember { mutableStateOf(false) }
    var adminIdInput by remember { mutableStateOf("") }
    var adminPasswordInput by remember { mutableStateOf("") }
    var adminLoginError by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF282218),
                        Color(0xFF1B1812)
                    )
                )
            )
            .border(
                width = 1.dp,
                brush = Brush.horizontalGradient(
                    colors = listOf(GoldDark, GoldPrimary, GoldDark)
                ),
                shape = androidx.compose.ui.graphics.RectangleShape
            )
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // App Logo & Title
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(GoldLight, GoldDark)
                            )
                        )
                        .border(1.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "श्री",
                        color = Color.Black,
                        fontWeight = FontWeight.Black,
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(
                        text = LocalizedStrings.get("app_title", currentLanguage),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = GoldPrimary,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = LocalizedStrings.get("tagline", currentLanguage),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = AccentGreen
                    )
                }
            }

            // Wallet Chip & Actions
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Wallet Chip
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFF2A251D))
                        .border(1.dp, GoldDark, RoundedCornerShape(20.dp))
                        .clickable { onWalletClick() }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountBalanceWallet,
                            contentDescription = "Wallet",
                            tint = GoldPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "₹${balance.toInt()}",
                            fontWeight = FontWeight.ExtraBold,
                            color = GoldLight,
                            fontSize = 13.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))

                // Language Selector
                Box {
                    IconButton(
                        onClick = { showLangMenu = true },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Text(
                            text = currentLanguage.flag,
                            fontSize = 18.sp
                        )
                    }

                    DropdownMenu(
                        expanded = showLangMenu,
                        onDismissRequest = { showLangMenu = false },
                        modifier = Modifier.background(Color(0xFF24201A))
                    ) {
                        AppLanguage.entries.forEach { lang ->
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(lang.flag)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(lang.displayName, color = Color.White)
                                    }
                                },
                                onClick = {
                                    onLanguageSelected(lang)
                                    showLangMenu = false
                                }
                            )
                        }
                    }
                }

                // Website Preview Button
                IconButton(
                    onClick = { onWebsiteClick() },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Language,
                        contentDescription = "Official Website",
                        tint = GoldPrimary
                    )
                }

                // Notification Icon
                IconButton(
                    onClick = { onNotificationsClick() },
                    modifier = Modifier.size(36.dp)
                ) {
                    BadgedBox(
                        badge = {
                            if (unreadNotificationsCount > 0) {
                                Badge(containerColor = GoldPrimary) {
                                    Text(
                                        text = unreadNotificationsCount.toString(),
                                        color = Color.Black,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = GoldLight
                        )
                    }
                }

                // User Registration / Account Icon
                IconButton(
                    onClick = { onAuthClick() },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "User Register & Login",
                        tint = GoldPrimary
                    )
                }

                // Admin Switch Toggle
                IconButton(
                    onClick = {
                        if (isAdmin) {
                            onAdminToggle(false)
                        } else {
                            adminIdInput = ""
                            adminPasswordInput = ""
                            adminLoginError = null
                            showAdminLoginDialog = true
                        }
                    },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AdminPanelSettings,
                        contentDescription = "Admin Toggle",
                        tint = if (isAdmin) GoldPrimary else Color.Gray
                    )
                }
            }
        }
    }

    if (showAdminLoginDialog) {
        AlertDialog(
            onDismissRequest = { showAdminLoginDialog = false },
            title = {
                Text(
                    text = "🌐 Web Admin Portal Login",
                    fontWeight = FontWeight.Bold,
                    color = GoldPrimary
                )
            },
            text = {
                Column {
                    Text(
                        text = "Enter Admin Credentials to open Web Portal Site (admin.shreematka.com)",
                        fontSize = 12.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = adminIdInput,
                        onValueChange = {
                            adminIdInput = it
                            adminLoginError = null
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
                        value = adminPasswordInput,
                        onValueChange = {
                            adminPasswordInput = it
                            adminLoginError = null
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
                    adminLoginError?.let { err ->
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = err, color = Color.Red, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (adminIdInput.trim() == "Abhi272005" && adminPasswordInput == "Abhishek272005@") {
                            showAdminLoginDialog = false
                            onAdminToggle(true)
                        } else {
                            adminLoginError = "❌ Invalid Admin ID or Password!"
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = Color.Black)
                ) {
                    Text("LOGIN ADMIN", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                Button(
                    onClick = { showAdminLoginDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray, contentColor = Color.White)
                ) {
                    Text("CANCEL")
                }
            },
            containerColor = Color(0xFF1E1B15),
            shape = RoundedCornerShape(16.dp)
        )
    }
}
