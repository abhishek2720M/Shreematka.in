package com.example.ui.screens

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary

@Composable
fun WebsitePreviewScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    var webViewRef: WebView? = remember { null }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B0F17))
    ) {
        // Top Web Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF161C28))
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = GoldPrimary
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(Color(0xFF0F172A), RoundedCornerShape(20.dp))
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Language,
                        contentDescription = null,
                        tint = GoldLight,
                        modifier = Modifier.padding(end = 6.dp)
                    )
                    Text(
                        text = "www.shreematkaplay.com",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(onClick = {
                webViewRef?.reload()
            }) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Reload",
                    tint = GoldPrimary
                )
            }
        }

        // Live WebView
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                // Programmatically pre-create WebView cache directories to avoid inaccessible cache errors
                try {
                    val cacheSubdirs = arrayOf(
                        "WebView",
                        "WebView/Default",
                        "WebView/Default/HTTP Cache",
                        "WebView/Default/HTTP Cache/Code Cache",
                        "WebView/Default/HTTP Cache/Code Cache/js"
                    )
                    for (dirPath in cacheSubdirs) {
                        val dir = java.io.File(ctx.cacheDir, dirPath)
                        if (!dir.exists()) {
                            dir.mkdirs()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                WebView(ctx).apply {
                    webViewClient = WebViewClient()
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    settings.allowFileAccess = true
                    settings.allowContentAccess = true
                    loadUrl("file:///android_asset/website/index.html")
                    webViewRef = this
                }
            },
            update = { view ->
                webViewRef = view
            }
        )
    }
}
