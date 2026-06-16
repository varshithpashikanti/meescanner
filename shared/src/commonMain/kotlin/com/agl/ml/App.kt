package com.appgolive.meescanner

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.agl.ml.designsystem.MSBgGrey
import com.agl.ml.history.ui.HistoryScreen
import com.agl.ml.history.viewmodel.HistoryViewModel
import com.appgolive.meescanner.document.DocumentViewModel
import com.appgolive.meescanner.home.ui.HomeScreen
import com.appgolive.meescanner.qr.viewmodel.QrViewModel
import com.appgolive.meescanner.result.ui.ResultScreen
import com.appgolive.meescanner.result.viewmodel.ResultViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun App(

) {


    MaterialTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets.safeDrawing,
            containerColor = MSBgGrey
        ){innerPadding ->
            val navController = rememberNavController()

            val resultViewmodel : ResultViewModel = koinViewModel()
            val qrViewModel : QrViewModel = koinViewModel()
            val documentViewModel : DocumentViewModel = koinViewModel()
            val historyViewModel : HistoryViewModel = koinViewModel()

            NavHost(
                navController = navController,
                startDestination = "Home",
                modifier = Modifier.padding(innerPadding)
            ){
                composable("Home") {
                    HomeScreen(
                        onScanResult = { result , analyzerType ->
                            resultViewmodel.setResult(result)
                            historyViewModel.storeResult(result,analyzerType)
                            navController.navigate("Result")
                        },
                        onHistoryClick = {
                            navController.navigate("History")
                        }
                    )
                }

                composable("Result") {
                    ResultScreen(resultViewmodel, qrViewModel,documentViewModel,
                        onBackClick = {
                            navController.popBackStack()
                        }
                    )

                }
                composable("History") {
                    HistoryScreen(
                        onBackClick = {
                            navController.popBackStack()
                        }
                    )
                }

            }
        }
    }
}
