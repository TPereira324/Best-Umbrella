package pt.iade.ei.bestumbrella1.view


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.coroutines.launch
import pt.iade.ei.bestumbrella1.di.AppModule
import pt.iade.ei.bestumbrella1.model.Station


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreenWithMarkers(navController: NavController, focusStation: String? = null) {
    val context = LocalContext.current
    val sessionManager = AppModule.provideSessionManager(context)
    val scope = rememberCoroutineScope()
    var rentalStartMs by remember { mutableStateOf<Long?>(null) }
    var rentalQr by remember { mutableStateOf<String?>(null) }
    var elapsedMs by remember { mutableStateOf(0L) }
    var showEndSheet by remember { mutableStateOf(false) }
    var selectedStation by remember { mutableStateOf<Station?>(null) }
    LaunchedEffect(focusStation) {}
    LaunchedEffect(Unit) {
        rentalStartMs = sessionManager.getRentalStartMs()
        rentalQr = sessionManager.getRentalQrCode()
    }
    val backStackEntry by navController.currentBackStackEntryAsState()
    LaunchedEffect(backStackEntry) {
        rentalStartMs = sessionManager.getRentalStartMs()
        rentalQr = sessionManager.getRentalQrCode()
    }
    LaunchedEffect(rentalStartMs) {
        if (rentalStartMs != null) {
            while (rentalStartMs != null) {
                elapsedMs = System.currentTimeMillis() - (rentalStartMs ?: 0L)
                kotlinx.coroutines.delay(1000)
            }
        }
    }



    Scaffold(
        bottomBar = { AppBottomNavigationBar(navController) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF2196F3).copy(alpha = 0.7f),
                            Color(0xFFE3F2FD)
                        )
                    )
                )
        ) {
            MapMarkersContent(
                focusStation = focusStation,
                selectedStation = selectedStation,
                onSelectStation = { selected -> selectedStation = selected },
                onReserved = {
                    scope.launch {
                        rentalStartMs = sessionManager.getRentalStartMs()
                        rentalQr = sessionManager.getRentalQrCode()
                    }
                }
            )

            if (rentalStartMs != null) {
                val totalSeconds = (elapsedMs / 1000).toInt()
                val h = totalSeconds / 3600
                val m = (totalSeconds % 3600) / 60
                val s = totalSeconds % 60
                UsageTimerFab(
                    h = h,
                    m = m,
                    s = s,
                    onClick = { showEndSheet = true },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 24.dp)
                        .shadow(8.dp, shape = MaterialTheme.shapes.medium)
                )
            }
            if (showEndSheet && rentalStartMs != null && rentalQr != null) {
                RentalEndSheet(
                    navController = navController,
                    rentalQr = rentalQr!!,
                    elapsedMs = elapsedMs,
                    onDismiss = { showEndSheet = false }
                )
            }
            if (rentalStartMs == null) {
                ScannerFab(
                    onClick = { navController.navigate("qrscanner") },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 24.dp)
                        .shadow(8.dp, shape = MaterialTheme.shapes.medium)
                )
            }
        }
    }
}
