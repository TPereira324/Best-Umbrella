package pt.iade.ei.bestumbrella1.view


import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import pt.iade.ei.bestumbrella1.model.Station


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreenWithMarkers(navController: NavController, focusStation: String? = null) {
    val context = LocalContext.current
    val paymentController = pt.iade.ei.bestumbrella1.di.AppModule.providePaymentController(context)
    val scope = rememberCoroutineScope()
    val rentalStartMs by paymentController.rentalStartMs.observeAsState()
    val rentalQr by paymentController.rentalQr.observeAsState()
    val elapsedMs by paymentController.elapsedMs.observeAsState(initial = 0L)
    var showEndSheet by remember { mutableStateOf(false) }
    var selectedStation by remember { mutableStateOf<Station?>(null) }
    LaunchedEffect(focusStation) {}
    LaunchedEffect(Unit) {
        paymentController.refreshRentalState()
        paymentController.startTimer()
    }
    val backStackEntry by navController.currentBackStackEntryAsState()
    LaunchedEffect(backStackEntry) {
        paymentController.refreshRentalState()
    }
    // Timer é gerido pelo PaymentController


    AppScreenScaffold(navController, topAlpha = 0.7f) {
        MapMarkersContent(
            focusStation = focusStation,
            selectedStation = selectedStation,
            onSelectStation = { selected -> selectedStation = selected },
            onReserved = {
                paymentController.refreshRentalState()
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
