package com.agl.ml.home.ui

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agl.ml.CameraPreview
import com.agl.ml.designsystem.MSBgGrey
import com.agl.ml.designsystem.MSBlack
import com.agl.ml.designsystem.MSGrey
import com.agl.ml.designsystem.MSPrimary
import com.agl.ml.designsystem.MSSpacing
import com.agl.ml.designsystem.MSWhite
import com.agl.ml.designsystem.TabPills
import com.agl.ml.home.util.ScanResult
import com.agl.ml.home.viewmodel.HomeViewModel
import com.agl.ml.qr.viewmodel.QrViewModel
import mlkittrial.shared.generated.resources.Res
import mlkittrial.shared.generated.resources.ic_scan
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun HomeScreen(modifier: Modifier = Modifier){

    val homeViewModel : HomeViewModel = koinViewModel()
    val qrViewModel: QrViewModel = koinViewModel()
    val qrContent by qrViewModel.qrContent.collectAsState()

    val availableApps by qrViewModel.availableApps.collectAsState()

    val selectedTab by homeViewModel.selectedTab.collectAsState()
    val analyzerType by homeViewModel.selectedAnalyzer.collectAsState()

    val scanResult by homeViewModel.scanResult.collectAsState()

    Box(modifier = modifier
        .fillMaxSize())
    {
        Column(modifier = Modifier.fillMaxSize().background(color = MSGrey)) {
            Column(modifier = Modifier.fillMaxWidth().weight(1f)) {
                Column(
                    modifier = Modifier.fillMaxWidth().weight(1f).background(color = MSWhite, shape = RoundedCornerShape(bottomStart = 40.dp , bottomEnd = 40.dp))
                ){

                    CameraPreview(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .clip(
                                RoundedCornerShape(
                                    bottomStart = 40.dp,
                                    bottomEnd = 40.dp
                                )
                            ),
                        analyzerType = analyzerType,
                        onScanResult = { result ->

                            when(result) {

                                is ScanResult.QrResult -> {
                                    qrViewModel.processQr(
                                        result.rawValue
                                    )
                                }

                                is ScanResult.TextResult -> {

                                }

                                is ScanResult.ObjectResult -> {

                                }

                                is ScanResult.DocumentResult -> {

                                }

                            }
                        }
                    )
                }
            }
            Column(modifier = Modifier.fillMaxWidth().padding(top = MSSpacing.xs, bottom = MSSpacing.xl)) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(MSSpacing.sm)
                ) {
                    TabPills(
                        tabs = listOf(
                            "QR Scanner",
                            "Object Detection",
                            "Text Analyzer",
                            "Document",
                            "Photo"
                        ),
                        selectedTab = selectedTab,
                        onTabSelected = {
                            homeViewModel.onTabSelected(it)
                        },
                        modifier = Modifier,
                        selectedBackgroundColor = MSBgGrey,
                        fontWeight = FontWeight.Normal,
                        selectedTextColor = MSPrimary,
                        letterSpacing = 1.sp
                    )
                }
                Spacer(modifier = Modifier.Companion.height(MSSpacing.lg))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Box(modifier = modifier
                        .background(
                            color = MSPrimary,
                            shape = RoundedCornerShape(100)
                        )
                        .padding(vertical = MSSpacing.lg , horizontal = MSSpacing.xxl)
                        .clickable {
                        }
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_scan),
                            modifier = modifier.size(20.dp),
                            contentDescription = null,
                            tint = MSBlack,
                        )
                    }

                }
            }
        }

        Box(
            modifier = Modifier.fillMaxSize().padding(MSSpacing.lg),
            contentAlignment = Alignment.BottomCenter
        ){
            ScanResultSection(
                analyzerType = analyzerType,
                qrContent = qrContent,
                availableApps = availableApps,
                modifier = Modifier
                    .padding(MSSpacing.md)
                    .fillMaxWidth()
                    .background(
                        MSWhite,
                        shape = RoundedCornerShape(
                            topStart = 20.dp,
                            topEnd = 20.dp,
                        )
                    )

            )
        }


    }
}