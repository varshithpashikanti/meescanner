package com.agl.ml.home.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.agl.ml.home.util.AnalyzerType
import com.agl.ml.home.util.QrContent
import com.agl.ml.qr.model.AppInfo
import com.agl.ml.qr.ui.QrResultCard

@Composable
fun ScanResultSection(
    analyzerType: AnalyzerType,
    qrContent: QrContent?,
    availableApps: List<AppInfo>,
    modifier: Modifier = Modifier
) {

    when(analyzerType) {

        AnalyzerType.QR -> {

            QrResultCard(
                qrContent = qrContent,
                availableApps = availableApps,
                modifier = modifier
            )
        }

        AnalyzerType.TEXT -> {

        }

        AnalyzerType.OBJECT -> {

        }

        AnalyzerType.DOCUMENT -> {

        }

        AnalyzerType.PHOTO -> {

        }
    }
}