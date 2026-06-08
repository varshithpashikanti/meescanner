package com.agl.ml.qr.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.agl.ml.designsystem.MSSpacing
import com.agl.ml.home.util.QrContent
import com.agl.ml.qr.model.AppInfo

@Composable
fun QrResultCard(
    qrContent: QrContent?,
    availableApps: List<AppInfo>,
    modifier: Modifier = Modifier
) {
    qrContent ?: return

    Column(modifier = modifier.padding(MSSpacing.lg)) {
        when (qrContent) {
            is QrContent.Upi -> {
                QrHeader(title = "UPI Payment", value = qrContent.rawValue)
                AppList(availableApps)
            }

            is QrContent.Url -> {
                QrHeader(title = "Website", value = qrContent.rawValue)
                AppList(availableApps)
            }

            is QrContent.Wifi -> QrHeader(title = "WiFi", value = qrContent.rawValue)
            is QrContent.Contact -> QrHeader(title = "Contact", value = qrContent.rawValue)
            is QrContent.Email -> QrHeader(title = "Email", value = qrContent.rawValue)
            is QrContent.Phone -> QrHeader(title = "Phone", value = qrContent.rawValue)
            is QrContent.Sms -> QrHeader(title = "SMS", value = qrContent.rawValue)
            is QrContent.PlainText -> QrHeader(title = "Text", value = qrContent.rawValue)
        }
    }
}

@Composable
private fun QrHeader(title: String, value: String) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
    Spacer(modifier = Modifier.height(MSSpacing.md))
}

@Composable
private fun AppList(availableApps: List<AppInfo>) {
    if (availableApps.isEmpty()) {
        Text(
            text = "No available apps found on this device to handle this content.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error
        )
    } else {
        Text(
            text = "Open with:",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        availableApps.forEach { app ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                // App Logo from ByteArray
                if (app.appLogo != null) {
                    AsyncImage(
                        model = app.appLogo,
                        contentDescription = app.appName,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        contentScale = ContentScale.Fit
                    )
                } else {
                    // Fallback if logo is missing
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (app.appName.isNotEmpty()) app.appName.take(1) else "?",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.DarkGray
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = app.appName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
