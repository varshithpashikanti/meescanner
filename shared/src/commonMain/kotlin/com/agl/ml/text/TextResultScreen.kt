package com.agl.ml.text

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.Bitmap
import coil3.compose.AsyncImage
import com.agl.ml.designsystem.MSGrey
import com.agl.ml.designsystem.MSSpacing


@Composable
fun TextResultScreen(text: String, frame: Bitmap?, modifier: Modifier) {
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

            SelectionContainer {
                Text(
                    text = text,
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.White
                )
            }




        }
    }

}