package com.agl.ml

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
import com.agl.ml.home.ui.HomeScreen

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

            NavHost(
                navController = navController,
                startDestination = "Home",
                modifier = Modifier.padding(innerPadding)
            ){
                composable("Home") {
                    HomeScreen()
                }

            }
        }




//        val detector = remember { ObjectDetection() }
//
//        var result by remember { mutableStateOf("Detecting...") }
//
//        val imageUrl = "https://c8.alamy.com/comp/2PNG8H3/qr-code-for-cashless-payment-at-the-stall-of-a-coconut-vendor-in-trichy-india-2PNG8H3.jpg"
//            LaunchedEffect(Unit) {
//            result = detector.detectFromUrl(imageUrl)
//        }
//
//        Column {
//            Text(result)
//        }


//        val brush = Brush.horizontalGradient(
//            colors = listOf(Color.Red, Color.Magenta , Color.Blue , Color.Green , Color.Yellow , Color.Transparent , Color.Cyan)
//        )


//        Box(
//            modifier = Modifier.fillMaxSize().background(Color.Black),
//        ){
//            CameraTap(
//                modifier = Modifier
//                    .fillMaxSize()
//            )
//
//        }


//        val brush = Brush.horizontalGradient(
//            colors = listOf(Color.Red, Color.Magenta , Color.Blue , Color.Green , Color.Yellow , Color.Transparent , Color.Cyan)
//        )
//        Box(
//            modifier = Modifier.fillMaxSize().background(Color.Black),
//        ){
//            Column(
//                modifier = Modifier.fillMaxSize().background(
//                    Color.Black
//                ),
//                horizontalAlignment = Alignment.CenterHorizontally,
//            ) {
//
//
//                    CameraPreview(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .weight(1f)
//                            .clip(
//                                RoundedCornerShape(
//                                    bottomStart = 30.dp,
//                                    bottomEnd = 30.dp
//                                )
//                            )
//                            .border(
//                                width = 2.dp,
//                                brush = brush,
//                                shape = RoundedCornerShape(
//                                    bottomStart = 30.dp,
//                                    bottomEnd = 30.dp
//                                )
//                            )
//                    )
//
//
//
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 45.dp),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Button(onClick = {}) {
//                        Text("Click Photo")
//                    }
//                }
//
//            }
//        }



    }



}
