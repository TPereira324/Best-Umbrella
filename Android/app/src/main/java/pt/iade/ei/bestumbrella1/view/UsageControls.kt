package pt.iade.ei.bestumbrella1.view

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.util.Locale

@Composable
fun UsageTimerFab(h: Int, m: Int, s: Int, onClick: () -> Unit, modifier: Modifier = Modifier) {
    androidx.compose.material3.ExtendedFloatingActionButton(
        onClick = onClick,
        containerColor = Color(0xFF1976D2),
        contentColor = Color.White,
        icon = {
            androidx.compose.material3.Icon(
                Icons.Default.History,
                contentDescription = null
            )
        },
        text = { androidx.compose.material3.Text("Terminar (%02d:%02d:%02d)".format(h, m, s)) },
        modifier = modifier
    )
}

@Composable
fun ScannerFab(onClick: () -> Unit, modifier: Modifier = Modifier) {
    androidx.compose.material3.ExtendedFloatingActionButton(
        onClick = onClick,
        containerColor = Color(0xFF1976D2),
        contentColor = Color.White,
        icon = {
            androidx.compose.material3.Icon(
                Icons.Default.QrCodeScanner,
                contentDescription = "Scanner"
            )
        },
        text = { androidx.compose.material3.Text("Scanner") },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RentalEndSheet(
    navController: NavController,
    rentalQr: String,
    elapsedMs: Long,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = androidx.compose.ui.platform.LocalContext.current
    val paymentController = pt.iade.ei.bestumbrella1.di.AppModule.providePaymentController(context)
    val totalSeconds = (elapsedMs / 1000).toInt()
    val hDisp = totalSeconds / 3600
    val mDisp = (totalSeconds % 3600) / 60
    val sDisp = totalSeconds % 60
    (((elapsedMs + 59999L) / 60000L).toInt()).coerceAtLeast(1)
    val baseFee = 0.30
    val ratePerMin = 0.15
    val amount = paymentController.computeAmount(elapsedMs)

    ModalBottomSheet(sheetState = sheetState, onDismissRequest = onDismiss) {
        androidx.compose.foundation.layout.Column(Modifier.padding(16.dp)) {
            Text("Terminar uso", color = Color.Black)
            Spacer(Modifier.height(8.dp))
            Text("Duração: %02d:%02d:%02d".format(hDisp, mDisp, sDisp), color = Color.Black)
            Text("Desbloqueio: €${"%.2f".format(baseFee)}", color = Color.Black)
            Text("Tarifa: €${"%.2f".format(ratePerMin)} / minuto", color = Color.Black)
            Text("Total a pagar: €${"%.2f".format(amount)}", color = Color(0xFF1B5E20))
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = {
                    onDismiss()
                    val valueStr = String.format(Locale.US, "%.2f", amount)
                    navController.navigate("payment/${rentalQr}/${valueStr}")
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
            ) {
                androidx.compose.material3.Icon(
                    Icons.Default.Payment,
                    contentDescription = null,
                    tint = Color.White
                )
                Spacer(Modifier.height(0.dp))
                Text("Pagar e terminar", color = Color.White)
            }
            Spacer(Modifier.height(8.dp))
            androidx.compose.material3.OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancelar")
            }
        }
    }
}
