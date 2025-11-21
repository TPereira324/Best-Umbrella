package pt.iade.ei.bestumbrella1.views

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ScannerStartButton(onStartClick: () -> Unit) {
    Button(
        onClick = onStartClick,
        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
            containerColor = Color(0xFF1976D2),
            contentColor = Color.White
        )
    ) {
        Icon(Icons.Default.CameraAlt, contentDescription = null, tint = Color.White)
        Spacer(Modifier.width(8.dp))
        Text("Iniciar Scanner", color = Color.White)
    }
}