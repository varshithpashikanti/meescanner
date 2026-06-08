package com.agl.ml.designsystem



import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun TabPills(
    tabs: List<String>,
    selectedTab: String,
    onTabSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    selectedBackgroundColor: Color = MSBgGrey,
    unselectedBackgroundColor: Color = Color.Transparent,
    textColor: Color = MSWhite,
    selectedTextColor: Color = MSPrimary,
    borderRadius: Dp = 30.dp,
    horizontalPadding: Dp = 15.dp,
    verticalPadding: Dp = 5.dp,
    spacing: Dp = 8.dp,
    letterSpacing: TextUnit = 0.sp,
    fontWeight: FontWeight = FontWeight.Normal,
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = modifier
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        tabs.forEach { tab ->
            TabPill(
                label = tab,
                isSelected = tab == selectedTab,
                onClick = { onTabSelected(tab) },
                selectedBackgroundColor = selectedBackgroundColor,
                unselectedBackgroundColor = unselectedBackgroundColor,
                textColor = textColor,
                borderRadius = borderRadius,
                horizontalPadding = horizontalPadding,
                verticalPadding = verticalPadding,
                letterSpacing = letterSpacing,
                fontWeight = fontWeight,
                selectedTextColor = selectedTextColor,
            )
        }
    }
}

@Composable
private fun TabPill(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    selectedBackgroundColor: Color,
    unselectedBackgroundColor: Color,
    textColor: Color,
    borderRadius: Dp,
    horizontalPadding: Dp,
    verticalPadding: Dp,
    letterSpacing: TextUnit = 0.sp,
    fontWeight: FontWeight = FontWeight.Normal,
    selectedTextColor: Color = Color.White,
) {
    val backgroundColor = if (isSelected) selectedBackgroundColor else unselectedBackgroundColor

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(borderRadius))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(horizontal = horizontalPadding, vertical = verticalPadding),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = fontWeight,
            color = if (isSelected) selectedTextColor else textColor,
            letterSpacing = letterSpacing
        )
    }
}

