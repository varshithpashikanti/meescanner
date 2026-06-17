package com.agl.ml.qr.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.agl.ml.designsystem.MSBgGrey
import com.agl.ml.designsystem.MSGrey
import com.agl.ml.designsystem.MSHorizontalDivider
import com.agl.ml.designsystem.MSSpacing
import com.agl.ml.designsystem.MSWhite
import com.agl.ml.designsystem.MsBanner
import com.agl.ml.qr.model.AppInfo
import com.agl.ml.qr.viewmodel.QrViewModel
import com.agl.ml.home.util.QrContent
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun QrResultScreen(
    modifier: Modifier = Modifier,
    qrViewModel: QrViewModel = koinViewModel(),
    onBackClick: () -> Unit
){
    val qrContent by qrViewModel
        .qrContent
        .collectAsState()

    val availableApps by qrViewModel
        .availableApps
        .collectAsState()

    QrResultCard(
        qrContent = qrContent,
        availableApps = availableApps,
        modifier = modifier
    )

}
@Composable
fun QrResultCard(
    qrContent: QrContent?,
    availableApps: List<AppInfo>,
    modifier: Modifier = Modifier
) {

    val showBanner = remember { mutableStateOf(false) }

    val onShowBanner = {
        showBanner.value = true
    }
    qrContent ?: return

    Box(modifier = modifier.fillMaxSize().background(
        color = MSGrey
    )) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(
                    bottomStart = 30.dp,
                    bottomEnd = 30.dp
                    ))
                .align(Alignment.TopStart)
        ){
            AsyncImage(
                model = qrContent.frame,
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
                .align(Alignment.BottomCenter)

        ){
            Column(
                modifier = modifier.fillMaxWidth()
            ) {
                when (qrContent) {

                    is QrContent.Upi -> {

                        if(availableApps.isEmpty()){
                            QrHeader(
                                title = "UPI" , value = qrContent.rawValue, onShowBanner = onShowBanner
                            )
                        }else{
                            AppList( availableApps)
                        }

                    }

                    is QrContent.Url -> {
                        if(availableApps.isEmpty()){
                            QrHeader(
                                title = "UPI" , value = qrContent.rawValue, onShowBanner = onShowBanner
                            )
                        }else{
                            AppList( availableApps)
                        }
                    }

                    is QrContent.Wifi -> QrHeader(title = "WiFi", value = qrContent.rawValue,onShowBanner = onShowBanner)
                    is QrContent.Contact -> QrHeader(title = "Contact", value = qrContent.rawValue,onShowBanner = onShowBanner)
                    is QrContent.Email -> QrHeader(title = "Email", value = qrContent.rawValue,onShowBanner = onShowBanner)
                    is QrContent.Phone -> QrHeader(title = "Phone", value = qrContent.rawValue,onShowBanner = onShowBanner)
                    is QrContent.Sms -> QrHeader(title = "SMS", value = qrContent.rawValue,onShowBanner = onShowBanner)
                    is QrContent.PlainText -> QrHeader(title = "Text", value = qrContent.rawValue,onShowBanner = onShowBanner)
                }
            }



        }

        MsBanner(
            isVisible = showBanner.value,
            onDismiss = { showBanner.value = false },
            info = "Link Copied to Clipboard",
        )

    }
}

@Composable
private fun QrHeader(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    onShowBanner: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(MSSpacing.md),
        verticalArrangement = Arrangement.spacedBy(MSSpacing.md)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = MSWhite,
            modifier = modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MSWhite
        )

        val clipboardManager = LocalClipboardManager.current


        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            Box(
                modifier = modifier
                    .background(
                        color = MSBgGrey,
                        shape = RoundedCornerShape(100)
                    )
                    .padding(vertical = MSSpacing.md , horizontal = MSSpacing.lg)
                    .clickable(
                        onClick = {
                            clipboardManager.setText(AnnotatedString(value))
                            onShowBanner()
                        }
                    )
            ){
                Text(
                    text = "Copy",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Normal,
                    color = MSWhite
                )
            }
        }
    }

}

@Composable
private fun AppList(availableApps: List<AppInfo> , modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(MSSpacing.lg),
    ){

        Text(
            text = "open with",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Normal,
            color = MSWhite,
            modifier = modifier.fillMaxWidth().align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.height(MSSpacing.md)
        )

        availableApps.forEachIndexed { index, app ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                if (app.appLogo != null) {
                    AsyncImage(
                        model = app.appLogo,
                        contentDescription = app.appName,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = app.appName,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = MSWhite
                )
            }
            if (index < availableApps.size - 1) {
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    thickness = 1.dp,
                    color = MSHorizontalDivider
                )
            }
        }
    }


}
