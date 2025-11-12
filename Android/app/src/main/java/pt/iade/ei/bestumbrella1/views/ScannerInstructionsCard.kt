package pt.iade.ei.bestumbrella1.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ScannerInstructionsCard() {
    Spacer(Modifier.height(50.dp))
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFBBDEFB))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Como usar:",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black
            )
            Spacer(Modifier.height(8.dp))
            Text("1. Dirija-se a uma estação Best Umbrella", color = Color.Black)
            Text("2. Toque em \"Iniciar Scanner\"", color = Color.Black)
            Text("3. Aponte a câmera para o código QR", color = Color.Black)
            Text("4. Aguarde o desbloqueio automático", color = Color.Black)
        }
    }
}