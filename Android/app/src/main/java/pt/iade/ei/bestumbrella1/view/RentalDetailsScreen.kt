package pt.iade.ei.bestumbrella1.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import pt.iade.ei.bestumbrella1.model.UmbrellaData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RentalDetailsScreen(
    navController: NavController,
    qrCode: String
) {
    val umbrella = remember(qrCode) { UmbrellaData.findByQrCode(qrCode) }
    val stationName = umbrella?.let { UmbrellaData.stationNameFor(it.pontoId) } ?: "Desconhecido"
    val context = androidx.compose.ui.platform.LocalContext.current
    val paymentController = pt.iade.ei.bestumbrella1.di.AppModule.providePaymentController(context)
    remember(umbrella, qrCode) { paymentController.priceForType(umbrella?.tipo) }
    val baseFee = 0.30
    val sessionManager = rememberSessionManager()
    val baseFeeStr = remember(baseFee) { paymentController.formatCurrency(baseFee) }
    val scope = androidx.compose.runtime.rememberCoroutineScope()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Detalhes do Aluguer",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                },
            )

        }
    ) { padding ->
        AppGradientBackground(topAlpha = 0.7f, modifier = Modifier
            .fillMaxSize()
            .padding(padding)) {
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
                        Text(
                            "Código do Guarda-Chuva:",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Text(
                            umbrella?.codigoQr ?: qrCode,
                            color = Color.Black,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(12.dp))
                        Text(
                            "QR para desbloqueio",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        val qrUrl = remember(qrCode) {
                            paymentController.qrUrlFor(umbrella?.codigoQr ?: qrCode, 256)
                        }
                        AsyncImage(
                            model = qrUrl,
                            contentDescription = null,
                            modifier = Modifier.size(180.dp)
                        )

                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Localização: $stationName",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Estado: ${umbrella?.estado ?: "Desconhecido"}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Cor: ${umbrella?.cor ?: "-"}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Tipo: ${umbrella?.tipo ?: "-"}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Tempo máximo: 24 horas",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "⚠️ Multa aplicada após 24h",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Red,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Registo: ${umbrella?.dataRegisto ?: "-"}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                        Spacer(Modifier.height(12.dp))
                        HorizontalDivider(thickness = 1.dp, color = Color(0xFFBBDEFB))
                        Spacer(Modifier.height(8.dp))
                        Text("Desbloqueio", fontWeight = FontWeight.Bold, color = Color.Black)
                        Text(
                            baseFeeStr,
                            color = Color(0xFF1B5E20),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Button(
                    onClick = {
                        val code = umbrella?.codigoQr ?: qrCode
                        scope.launch { sessionManager.startRental(code) }
                        navController.navigate("map")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
                ) {
                    Text("Usar agora", color = Color.White, fontWeight = FontWeight.Bold)
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
