package com.agl.ml.designsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import mlkittrial.shared.generated.resources.Res
import mlkittrial.shared.generated.resources.ic_close
import org.jetbrains.compose.resources.painterResource
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

enum class BannerMessageType {
    SUCCESS,
    FAILURE,
    INFO
}

enum class BannerMessagePosition {
    TOP,
    BOTTOM
}

@Composable
fun MsBanner(
    isVisible: Boolean,
    info: String,
    onDismiss: () -> Unit,
    autoDismiss : Boolean = true,
    type: BannerMessageType = BannerMessageType.SUCCESS,
    duration: Duration = 2.seconds,
    position: BannerMessagePosition = BannerMessagePosition.TOP,
    modifier: Modifier = Modifier
) {

    val brush = Brush.linearGradient(
        colors = listOf(
            Color(0xFF1D46A4),
            Color(0xFF35B8DE),
            Color(0xFF2691B8),
            Color(0xFF7D74DA),
            Color(0xFFBB68BC),
            Color(0xFFD27475),
            Color(0xFFE9C58A),
        )
    )

    LaunchedEffect(isVisible) {
        if (isVisible && autoDismiss) {
            delay(duration.inWholeMilliseconds)
            onDismiss()
        }
    }

    if (isVisible) {
        val messageColor = when (type) {
            BannerMessageType.SUCCESS -> Color.Green
            BannerMessageType.FAILURE -> Color.Red
            BannerMessageType.INFO -> Color(0xFFFBC02D)
        }

        val alignment = when (position) {
            BannerMessagePosition.TOP -> Alignment.TopCenter
            BannerMessagePosition.BOTTOM -> Alignment.BottomCenter
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = alignment
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth(0.9f)
                    .padding(
                        top = if (position == BannerMessagePosition.TOP) MSSpacing.md else 0.dp,
                        bottom = if (position == BannerMessagePosition.BOTTOM) MSSpacing.md else 0.dp,
                        start = MSSpacing.md,
                        end = MSSpacing.md
                    )
                    .clip(RoundedCornerShape(12.dp))
                    .border(
                        width = 1.dp,
                        brush = brush,
                        shape = RoundedCornerShape(50)
                    )
                    .background(
                        color = MSBgGrey,
                        shape = RoundedCornerShape(50)
                    )
                    .padding(horizontal = MSSpacing.md, vertical = MSSpacing.sm),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(MSSpacing.sm)
            ) {
                Text(
                    text = info,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MSWhite
                )
                Icon(
                    painter = painterResource(Res.drawable.ic_close),
                    contentDescription = "Close",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onDismiss() },
                    tint = MSWhite
                )
            }
        }
    }
}