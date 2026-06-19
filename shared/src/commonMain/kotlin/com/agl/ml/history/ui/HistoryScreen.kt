package com.agl.ml.history.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import org.koin.compose.viewmodel.koinViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import com.appgolive.meescanner.entity.ScanHistoryEntity
import mlkittrial.shared.generated.resources.pdf
import mlkittrial.shared.generated.resources.qr_code
import mlkittrial.shared.generated.resources.recognition
import mlkittrial.shared.generated.resources.text

@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier,
    historyViewModel: HistoryViewModel = koinViewModel(),
    onBackClick: () -> Unit
){

    val showDetailSheet by remember { mutableStateOf(false) }
    val selectedFilterTab by historyViewModel.selectedFilterTab.collectAsState()

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

            when(val state = historyUiState){
                is HistoryUiState.Success -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(MSSpacing.sm),
                    ){
                        items(filteredHistory){history ->
                            HistoryItem(history)
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
    }
}

@Composable
fun HistoryItem(history: ScanHistoryEntity) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = MSSpacing.lg)
            .background(
                color = MSBgGrey,
                shape = RoundedCornerShape(30.dp)
            )
            .padding(MSSpacing.md),
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
            modifier = Modifier.width(60.dp).aspectRatio(3f/4f)
        ){
            if (image != null) {
                Image(
                    painter = painterResource(image),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize().background(color = MSWhite, shape = RoundedCornerShape(20.dp)).padding(horizontal = MSSpacing.sm)
                )
            }
        }

        Column(
            modifier = Modifier.weight(1f)
        ){
            Text(
                text = history.title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = MSWhite,
            )
            Text(
                text = history.data,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Normal,
                color = MSWhite,
            )

        }

    }
}