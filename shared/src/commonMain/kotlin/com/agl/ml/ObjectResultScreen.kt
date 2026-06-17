package com.agl.ml.result.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.Bitmap
import coil3.compose.AsyncImage
import com.agl.ml.designsystem.MSGrey
import com.agl.ml.designsystem.MSSpacing
import com.agl.ml.designsystem.MSWhite
import com.agl.ml.home.util.DetectedObjectInfo

@Composable
fun ObjectResultScreen(
    modifier: Modifier = Modifier,
    frame: Bitmap?,
    objects: List<DetectedObjectInfo>
) {

    Box(modifier = modifier.fillMaxSize().background(
        color = MSGrey
    )) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(
                        bottomStart = 30.dp,
                        bottomEnd = 30.dp
                    )
                )
                .align(Alignment.TopStart)
        ) {
            AsyncImage(
                model = frame,
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth,
                alignment = Alignment.TopStart

            )

        }


        Box(
            modifier = modifier
            .fillMaxWidth()
            .background(
                color = MSGrey,
                shape = RoundedCornerShape(
                    topStart = 40.dp,
                    topEnd = 40.dp
                )
            )
                .padding(vertical = MSSpacing.lg , horizontal = MSSpacing.sm)
            .align(Alignment.BottomCenter)
        ) {

            if (objects.isEmpty()) {    Text("No objects detected", modifier = Modifier.align(Alignment.Center), color = Color.White)
            }
            else{
                LazyRow(
                    modifier = modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(vertical = MSSpacing.xl),
                    contentPadding = PaddingValues(horizontal = MSSpacing.sm),
                    horizontalArrangement = Arrangement.spacedBy(MSSpacing.sm)
                ) {
                    items(objects) { obj ->
                        ObjectCard(obj = obj )
                    }
                }
            }


        }
    }



}

@Composable
private fun ObjectCard(obj: DetectedObjectInfo , modifier: Modifier = Modifier) {

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

    Row(
        modifier = modifier
            .border(
                width = 1.dp,
                brush = brush,
                shape = RoundedCornerShape(24.dp)
            )
            .background(
                color = MSGrey,
                shape = RoundedCornerShape(24.dp)
            )
            .padding(MSSpacing.sm),
        verticalAlignment = Alignment.CenterVertically
    ){
        AsyncImage(
            model = obj.croppedImage,
            contentDescription = null,
            modifier = modifier
                .width(120.dp)
                .aspectRatio(4f/5f)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(MSSpacing.md))
        Text(
            text = obj.label,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Medium,
            color = MSWhite
        )
        Spacer(modifier = Modifier.width(MSSpacing.sm))
    }
}