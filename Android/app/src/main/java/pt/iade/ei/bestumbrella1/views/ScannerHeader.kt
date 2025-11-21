package pt.iade.ei.bestumbrella1.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ScannerHeader() {
    Column {
        Spacer(Modifier.height(8.dp))
        Text(
            "Scanner QR",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.Black
        )
        Spacer(Modifier.height(50.dp))
        Text(
            "Escaneie o código QR do guarda-chuva para desbloquear",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black
        )
        Spacer(Modifier.height(50.dp))
        Icon(
            Icons.Default.QrCodeScanner,
            contentDescription = null,
            modifier = Modifier
                .size(96.dp)
                .align(Alignment.CenterHorizontally),
            tint = Color.Black
        )
        Spacer(Modifier.height(16.dp))
        Text(
            "Pronto para escanear",
            style = MaterialTheme.typography.titleMedium,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            "Toque no botão abaixo para ativar a câmera",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(35.dp))
    }
}