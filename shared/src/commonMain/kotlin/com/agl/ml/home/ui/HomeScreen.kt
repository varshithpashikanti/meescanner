package com.appgolive.meescanner.home.ui

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agl.ml.designsystem.MSBgGrey
import com.agl.ml.designsystem.MSBlack
import com.agl.ml.designsystem.MSGrey
import com.agl.ml.designsystem.MSPrimary
import com.agl.ml.designsystem.MSSpacing
import com.agl.ml.designsystem.MSWhite
import com.agl.ml.designsystem.TabPills
import com.agl.ml.document.DocumentScanner
import com.appgolive.meescanner.CameraPreview
import com.appgolive.meescanner.document.DocumentViewModel
import com.appgolive.meescanner.home.util.AnalyzerType
import com.appgolive.meescanner.home.util.ScanResult
import com.appgolive.meescanner.home.viewmodel.HomeViewModel
import mlkittrial.shared.generated.resources.Res
import mlkittrial.shared.generated.resources.ic_history
import mlkittrial.shared.generated.resources.ic_music
import mlkittrial.shared.generated.resources.ic_scan
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun HomeScreen(
    onScanResult: (ScanResult, AnalyzerType) -> Unit,
    onHistoryClick: () -> Unit,
    modifier: Modifier = Modifier,
){

    val homeViewModel : HomeViewModel = koinViewModel()

    val documentViewModel : DocumentViewModel =  koinViewModel()

    val openScanner by documentViewModel.openScanner.collectAsState()

    DocumentScanner(
        trigger = openScanner,
        onResult = {
            documentViewModel.onScanSuccess(it)
            onScanResult(
                ScanResult.DocumentResult(
                    pageCount = it.pageCount,
                    pdfUri = it.pdfUri,
                    pages = it.pages
                ),
                AnalyzerType.DOCUMENT
            )
        },
        onError = {
            documentViewModel.onScanError(it)
        }
    )

    val selectedTab by homeViewModel.selectedTab.collectAsState()
    val analyzerType by homeViewModel.selectedAnalyzer.collectAsState()

    val triggerCapture by homeViewModel.triggerCapture.collectAsState()

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
                            onScanResult(result, analyzerType)
                        },
                        triggerCapture = triggerCapture,
                        onCaptureCompleted = { homeViewModel.onCaptureCompleted() }
                    )
                }
            }


            Column(modifier = Modifier.fillMaxWidth().padding(top = MSSpacing.xs, bottom = MSSpacing.xl)) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = MSSpacing.sm)
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
                        onTabSelected = { tab ->

                            if(tab == "Document") {
                                documentViewModel.onDocumentTabTapped()
                            }else{
                                homeViewModel.onTabSelected(tab)
                            }
                        },
                        modifier = Modifier,
                        selectedBackgroundColor = MSBgGrey,
                        unselectedBackgroundColor = MSBgGrey,
                        fontWeight = FontWeight.Normal,
                        selectedTextColor = MSPrimary,
                        letterSpacing = 1.sp,
                        verticalPadding = MSSpacing.md
                    )
                }
                Spacer(modifier = Modifier.Companion.height(MSSpacing.lg))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = MSSpacing.lg),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Box(modifier = modifier
                        .background(
                            color = MSBgGrey,
                            shape = RoundedCornerShape(100)
                        )
                        .padding(vertical = MSSpacing.md , horizontal = MSSpacing.xl)
                        .clickable {

                        }
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_music),
                            modifier = modifier.size(24.dp),
                            contentDescription = null,
                            tint = MSWhite,
                        )
                    }

                    Box(modifier = modifier
                        .background(
                            color = MSPrimary,
                            shape = RoundedCornerShape(100)
                        )
                        .clickable {

                            if(selectedTab == "Object Detection" || selectedTab == "Text Analyzer") {
                                homeViewModel.onCaptureTrigger()
                            }

                        }
                        .padding(vertical = MSSpacing.xl , horizontal = MSSpacing.xxxl)

                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_scan),
                            modifier = modifier.size(24.dp),
                            contentDescription = null,
                            tint = MSBlack,
                        )
                    }

                    Box(modifier = modifier
                        .background(
                            color = MSBgGrey,
                            shape = RoundedCornerShape(100)
                        )
                        .clickable {
                            onHistoryClick()
                        }
                        .padding(vertical = MSSpacing.md , horizontal = MSSpacing.xl)

                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_history),
                            modifier = modifier.size(24.dp),
                            contentDescription = null,
                            tint = MSWhite,
                        )
                    }


                }
            }
        }

        Text(
            text = "MeeScanner",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Medium,
            color = MSWhite,
            modifier = Modifier.align(Alignment.TopStart).padding(top = MSSpacing.lg , start = MSSpacing.lg)
        )


    }
}