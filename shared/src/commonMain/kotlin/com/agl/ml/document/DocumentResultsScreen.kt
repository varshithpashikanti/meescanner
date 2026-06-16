package com.agl.ml.document

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.agl.ml.designsystem.MSGrey
import com.agl.ml.designsystem.MSSpacing

@Composable
fun DocumentResultScreen(
    uri: String,
    pages: Int,
    modifier: Modifier,
    allPages: List<ScannedDocumentPage>
) {
    Box(modifier = modifier.fillMaxSize().background(
        color = MSGrey
    )) {



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

            Box(
                modifier = Modifier.align(Alignment.Center).padding(horizontal = MSSpacing.xxxl)
            ){
                allPages.forEach {
                    DocumentPage(uri = it.imageUri)
                }
            }

           Text(
               text = uri,
               modifier = Modifier.align(Alignment.Center),
               color = Color.White
           )
            Text(
                text = pages.toString(),
                modifier = Modifier.align(Alignment.Center),
                color = Color.White
            )

        }
    }

}

@Composable
fun DocumentPage(
    uri: String,
    modifier: Modifier = Modifier
){
    AsyncImage(
        model = uri,
        contentDescription = null,
        modifier = modifier.fillMaxWidth()
    )
}