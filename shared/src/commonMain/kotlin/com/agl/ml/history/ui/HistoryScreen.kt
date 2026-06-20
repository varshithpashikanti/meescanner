package com.agl.ml.history.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.agl.ml.history.viewmodel.HistoryViewModel
import mlkittrial.shared.generated.resources.Res
import mlkittrial.shared.generated.resources.ic_arrow_back
import mlkittrial.shared.generated.resources.ic_search
import org.jetbrains.compose.resources.painterResource
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import com.agl.ml.designsystem.MSDimensions
import com.agl.ml.designsystem.MSLightGrey
import com.agl.ml.designsystem.MsBanner
import com.agl.ml.home.util.AnalyzerType
import com.agl.ml.home.util.QrContent
import com.agl.ml.home.util.QrContentAnalyzer
import com.agl.ml.qr.viewmodel.QrViewModel
import com.appgolive.meescanner.entity.ScanHistoryEntity
import mlkittrial.shared.generated.resources.ic_heart
import mlkittrial.shared.generated.resources.ic_history
import mlkittrial.shared.generated.resources.ic_share
import mlkittrial.shared.generated.resources.pdf
import mlkittrial.shared.generated.resources.qr_code
import mlkittrial.shared.generated.resources.recognition
import mlkittrial.shared.generated.resources.text

@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier,
    historyViewModel: HistoryViewModel,
    onBackClick: () -> Unit,
    qrViewModel: QrViewModel
){

    val selectedFilterTab by historyViewModel.selectedFilterTab.collectAsState()

    val selectedHistory by historyViewModel.selectedHistory.collectAsState()
    val showDetailSheet by historyViewModel.showDetailSheet.collectAsState()

    val tabList = listOf("ALL" , "QR" , "TEXT" , "OBJECT" , "DOCUMENT" , "PHOTO")
    LaunchedEffect(Unit){
        historyViewModel.loadHistory()
    }
    val historyUiState by historyViewModel.historyUiState.collectAsState()
    val history = historyViewModel.historyUiState.value
    val filteredHistory = when(history){
        is HistoryUiState.Success -> {
            if(selectedFilterTab == "ALL") history.history else
                history.history.filter { it.scanType == selectedFilterTab }
        }
        else -> emptyList()
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = MSGrey
            )
    ){
        Column(
            modifier = Modifier.fillMaxSize().padding(top = MSSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(MSSpacing.lg)
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding( horizontal = MSSpacing.lg),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Row(
                    horizontalArrangement = Arrangement.spacedBy(MSSpacing.md),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_arrow_back),
                        contentDescription = null,
                        tint = MSPrimary,
                        modifier = modifier.size(36.dp).clickable{
                            onBackClick()
                        }
                    )

                    Text(
                        text = "MeeScanner",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MSWhite,
                    )
                }

                Box(
                    modifier = modifier
                        .background(
                            color = MSBgGrey,
                            shape = RoundedCornerShape(100)
                        )
                        .padding(MSSpacing.md)
                ){
                    Icon(
                        painter = painterResource(Res.drawable.ic_search),
                        modifier = modifier.size(18.dp),
                        contentDescription = null,
                        tint = MSWhite,
                    )
                }
            }

            TabPills(
                tabs = tabList,
                selectedTab = selectedFilterTab,
                onTabSelected = {
                    historyViewModel.onTabSelected(it)
                },
                modifier = Modifier,
                selectedBackgroundColor = MSWhite,
                unselectedBackgroundColor = MSBgGrey,
                style = MaterialTheme.typography.bodyLarge,
                selectedTextColor = MSBlack,
                textColor = MSWhite,
                letterSpacing = 1.sp,
                verticalPadding = MSSpacing.md,
                borderRadius = 40.dp,
                applyBorder = false,
                spacing = MSSpacing.sm
            )
            Spacer(modifier = Modifier.height(MSSpacing.xs))
            when(val state = historyUiState){
                is HistoryUiState.Success -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(MSSpacing.sm),
                    ){
                        items(filteredHistory){history ->
                            HistoryItem(
                                history,
                                modifier = Modifier.clickable {
                                    historyViewModel.onHistorySelected(history)
                                },
                            )
                        }
                    }
                }
                is HistoryUiState.Error -> {
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = (historyUiState as HistoryUiState.Error).message,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = MSWhite,
                        )
                    }
                }
                is HistoryUiState.Loading -> {
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Loading...",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = MSWhite,
                        )
                    }
                }
            }

        }

        if(showDetailSheet){
            DetailBottomSheet(
                history = selectedHistory!!,
                qrViewModel = qrViewModel,
                onDismiss = {
                    historyViewModel.historyDismiss()
                }
            )
        }

    }
}


@Composable
fun HistoryItem(
    history: ScanHistoryEntity,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = MSSpacing.lg)
            .background(
                color = MSBgGrey,
                shape = RoundedCornerShape(30.dp)
            )
            .padding(MSSpacing.md)
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(MSSpacing.sm),
        verticalAlignment = Alignment.CenterVertically
    ){
        val image  = when(history.scanType){
            "QR" -> Res.drawable.qr_code
            "TEXT" -> Res.drawable.text
            "OBJECT" -> Res.drawable.recognition
            "DOCUMENT" -> Res.drawable.pdf
            else -> null
        }

        Box(
            modifier = modifier.width(60.dp).aspectRatio(3f/4f)
        ){
            if (image != null) {
                Image(
                    painter = painterResource(image),
                    contentDescription = null,
                    modifier = modifier.fillMaxSize().background(color = MSWhite, shape = RoundedCornerShape(20.dp)).padding(horizontal = MSSpacing.sm)
                )
            }
        }

        Column(
            modifier = modifier.weight(1f).fillMaxHeight().padding(MSSpacing.sm),
            verticalArrangement = Arrangement.SpaceBetween,

        ){
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = history.title,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = MSWhite,
                )

                Icon(
                    painter = painterResource(Res.drawable.ic_heart),
                    contentDescription = null,
                    tint = MSWhite,
                    modifier = modifier.size(MSDimensions.Icon.medium)
                )
            }
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(
                    painter = painterResource(Res.drawable.ic_share),
                    contentDescription = null,
                    tint = MSLightGrey,
                    modifier = modifier.size(MSDimensions.Icon.medium)
                )
                Text(
                    text = history.createdAt.toFormattedDateTime(),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Normal,
                    color = MSLightGrey,
                )
            }


        }

    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailBottomSheet(
    history: ScanHistoryEntity,
    qrViewModel: QrViewModel,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {},
) {

    val clipboardManager = LocalClipboardManager.current

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MSGrey,
        scrimColor = Color.Black.copy(alpha = 0.45f),
        shape = RoundedCornerShape(
            topStart = 28.dp,
            topEnd = 28.dp
        ),
        dragHandle = {
            BottomSheetDefaults.DragHandle(color = Color.Gray)
        }
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.75f)
                .navigationBarsPadding()
                .imePadding()
        ) {
            Column(
                modifier = Modifier.padding(
                    horizontal = MSSpacing.lg,
                    vertical = MSSpacing.md
                )
            ) {

                Text(
                    text = history.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MSWhite
                )
            }

            HorizontalDivider(
                color = MSBgGrey
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(MSSpacing.md),
                verticalArrangement = Arrangement.spacedBy(MSSpacing.md)
            ) {

                item {

                    Text(
                        text = if (history.scanType == AnalyzerType.QR.name) "URL"
                        else if(history.scanType == AnalyzerType.TEXT.name) "Text"
                        else if(history.scanType == AnalyzerType.OBJECT.name) "Objects"
                        else "Document Url",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MSLightGrey
                    )
                    Spacer(Modifier.height(MSSpacing.sm))

                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .background(MSBgGrey, RoundedCornerShape(16.dp))
                            .padding(MSSpacing.sm),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        SelectionContainer {
                            Text(
                                text = history.data,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MSWhite
                            )
                        }
                    }
                }

                item {

                    Text(
                        text = "Scan Type",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MSLightGrey
                    )

                    Spacer(Modifier.height(MSSpacing.sm))

                    Text(
                        text = history.scanType,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MSWhite
                    )
                }

                item {

                    Text(
                        text = "Scanned At",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MSLightGrey
                    )

                    Spacer(Modifier.height(MSSpacing.sm))

                    Text(
                        text = history.createdAt.toString(),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MSWhite
                    )
                }

                item {
                    Spacer(Modifier.height(MSSpacing.md))
                }
            }

            HorizontalDivider(
                color = MSBgGrey
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = MSSpacing.md),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                if (history.scanType == AnalyzerType.QR.name || history.scanType == AnalyzerType.DOCUMENT.name) {
                    Box(modifier = modifier
                        .background(
                            color = MSBgGrey,
                            shape = RoundedCornerShape(100)
                        )
                        .clickable {
                            qrViewModel.processQr(
                                rawValue = history.data,
                                frame = null
                            )
                        }
                        .padding(vertical = MSSpacing.md , horizontal = MSSpacing.xl)

                    ) {
                        Text(
                            "Open",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                    Spacer(modifier = Modifier.width(MSSpacing.xl))
                }


                Box(modifier = modifier
                    .background(
                        color = MSBgGrey,
                        shape = RoundedCornerShape(100)
                    )
                    .clickable {
                        clipboardManager.setText(
                            AnnotatedString(history.data)
                        )
                    }
                    .padding(vertical = MSSpacing.md , horizontal = MSSpacing.xl)

                ) {
                    Text(
                        "Copy",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }


        }
    }
}