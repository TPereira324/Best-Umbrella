package pt.iade.ei.bestumbrella1.views

import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.remember
import pt.iade.ei.bestumbrella1.models.UmbrellaData
import coil.compose.AsyncImage
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RentalDetailsScreen(
    navController: NavController,
    qrCode: String
) {
    val umbrella = remember(qrCode) { UmbrellaData.findByQrCode(qrCode) }
    val stationName = umbrella?.let { UmbrellaData.stationNameFor(it.pontoId) } ?: "Desconhecido"
    val price = remember(umbrella, qrCode) {
        when (umbrella?.tipo?.lowercase(Locale.ROOT)) {
            "automático" -> 3.49
            "compacto" -> 2.99
            "manual" -> 2.49
            else -> 2.99
        }
    }
    val priceStr = remember(price) { NumberFormat.getCurrencyInstance(Locale("pt", "PT")).format(price) }
    val baseFee = 0.30
    val baseFeeStr = remember(baseFee) { NumberFormat.getCurrencyInstance(Locale("pt", "PT")).format(baseFee) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhes do Aluguer", color = Color.Black, fontWeight = FontWeight.Bold) },
                )

        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF2196F3),
                            Color(0xFFE3F2FD).copy(alpha = 0.7f)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Resumo do Aluguer",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.Black
                )

                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 2.dp,
                    color = Color(0xFF90CAF9)
                )
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Código do Guarda-Chuva:", fontWeight = FontWeight.Bold, color = Color.Black)
                        Text(umbrella?.codigoQr ?: qrCode, color = Color.Black, fontSize = 18.sp, fontWeight = FontWeight.Bold)

                        Spacer(Modifier.height(12.dp))
                        Text("QR para desbloqueio", fontWeight = FontWeight.Bold, color = Color.Black)
                        val qrUrl = remember(qrCode) { 
                            val base = pt.iade.ei.bestumbrella1.BuildConfig.API_BASE_URL.removeSuffix("/")
                            val origin = base.removeSuffix("/api")
                            "$origin/api/guardachuvas/codigo/${umbrella?.codigoQr ?: qrCode}/qrcode?size=256"
                        }
                        AsyncImage(
                            model = qrUrl,
                            contentDescription = null,
                            modifier = Modifier.size(180.dp)
                        )

                        Spacer(Modifier.height(8.dp))
                        Text("Localização: $stationName", style = MaterialTheme.typography.bodyMedium, color = Color.Black, fontWeight = FontWeight.Bold)
                        Text("Estado: ${umbrella?.estado ?: "Desconhecido"}", style = MaterialTheme.typography.bodyMedium, color = Color.Black, fontWeight = FontWeight.Bold)
                        Text("Cor: ${umbrella?.cor ?: "-"}", style = MaterialTheme.typography.bodyMedium, color = Color.Black, fontWeight = FontWeight.Bold)
                        Text("Tipo: ${umbrella?.tipo ?: "-"}", style = MaterialTheme.typography.bodyMedium, color = Color.Black, fontWeight = FontWeight.Bold)
                        Text("Tempo máximo: 24 horas", style = MaterialTheme.typography.bodyMedium, color = Color.Black, fontWeight = FontWeight.Bold)
                        Text("⚠️ Multa aplicada após 24h", style = MaterialTheme.typography.bodySmall, color = Color.Red, fontWeight = FontWeight.Bold)
                        Text("Registo: ${umbrella?.dataRegisto ?: "-"}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        Spacer(Modifier.height(12.dp))
                        HorizontalDivider(thickness = 1.dp, color = Color(0xFFBBDEFB))
                        Spacer(Modifier.height(8.dp))
                        Text("Desbloqueio", fontWeight = FontWeight.Bold, color = Color.Black)
                        Text(baseFeeStr, color = Color(0xFF1B5E20), fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(Modifier.height(20.dp))

                Button(
                    onClick = { navController.navigate("payment/${umbrella?.codigoQr ?: qrCode}/${String.format(Locale.US, "%.2f", price)}") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
                ) {
                    Text("Pagar e Desbloquear", color = Color.White, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF1976D2))
                ) {
                    Text("Cancelar", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
