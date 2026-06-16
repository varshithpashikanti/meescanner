package com.agl.ml.designsystem



import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
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
    horizontalPadding: Dp = MSSpacing.lg,
    verticalPadding: Dp = MSSpacing.sm,
    spacing: Dp = MSSpacing.sm,
    letterSpacing: TextUnit = 0.sp,
    fontWeight: FontWeight = FontWeight.Normal,
    applyBorder: Boolean = true,
    style : TextStyle = MaterialTheme.typography.labelSmall
) {

    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = horizontalPadding),
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        tabs.forEach { tab ->
            item {
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
                    applyBorder = applyBorder,
                    style = style
                )
            }

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
    applyBorder: Boolean = true,
    style : TextStyle
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

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(borderRadius))
            .background(if(isSelected) selectedBackgroundColor else unselectedBackgroundColor)
            .then(
                if (isSelected && applyBorder) {
                    Modifier.border(
                        width = 1.dp,
                        brush = brush,
                        shape = RoundedCornerShape(borderRadius)
                    )
                } else {
                    Modifier // Empty modifier, does nothing
                }
            )
//            .border(
//                width = 1.dp,
//                brush = brush,
//                shape = RoundedCornerShape(borderRadius)
//            )
            .clickable { onClick() }
            .padding(horizontal = horizontalPadding, vertical = verticalPadding),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = style,
            color = if (isSelected) selectedTextColor else textColor,
            letterSpacing = letterSpacing
        )
    }
}

