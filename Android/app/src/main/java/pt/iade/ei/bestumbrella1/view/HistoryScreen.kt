package pt.iade.ei.bestumbrella1.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pt.iade.ei.bestumbrella1.model.RentalEntry
import pt.iade.ei.bestumbrella1.model.extractHours
import pt.iade.ei.bestumbrella1.model.historySortKey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(navController: NavController) {
    val entries = listOf(
        RentalEntry("Hoje, 14:30", "Metro Moscavide", "Parque das Nações", 0.29, "35 min"),
        RentalEntry("Ontem, 09:16", "Vasco da Gama Shopping", "Metro Oriente", 1.00, "1h 15min"),
        RentalEntry("Há 2 dias", "IADE", "Metro Oriente", 2.50, "27h 40min"),
        RentalEntry("Hoje, 11:05", "Terreiro do Paço", "Baixa-Chiado", 0.35, "22 min"),
        RentalEntry("Hoje, 09:20", "Rossio", "Marquês de Pombal", 0.50, "41 min"),
        RentalEntry("Ontem, 18:10", "Baixa-Chiado", "Rossio", 0.25, "17 min"),
        RentalEntry("Há 3 dias", "Marquês de Pombal", "Terreiro do Paço", 1.20, "26h 05min")
    )
    val sortedEntries = remember(entries) { entries.sortedByDescending { historySortKey(it.date) } }

    AppScreenScaffold(navController, topAlpha = 0.6f) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                "Histórico",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Spacer(Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFBBDEFB))
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "7",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Usos",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Black
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "5h 45min",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Tempo Total",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Black
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "€2.88",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Gasto Total",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Black
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            LazyColumn {
                items(sortedEntries) { entry ->
                    val durationHours = extractHours(entry.duration)
                    val multa = if (durationHours > 24) 100.0 else 0.0
                    val totalCost = entry.cost + multa

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFBBDEFB))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "${entry.date} — Concluído",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(onClick = {
                                    navController.navigateToMapFocus(entry.from)
                                }) {
                                    Icon(
                                        Icons.Default.LocationOn,
                                        tint = Color(0xFF2196F3),
                                        contentDescription = null
                                    )
                                }
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    "De: ${entry.from}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Black
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(onClick = {
                                    navController.navigateToMapFocus(entry.to)
                                }) {
                                    Icon(
                                        Icons.Default.LocationOn,
                                        tint = Color(0xFFF44336),
                                        contentDescription = null
                                    )
                                }
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    "Para: ${entry.to}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Black
                                )
                            }
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "Duração: ${entry.duration}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Black
                            )
                            Text(
                                "Custo base: €${"%.2f".format(entry.cost)}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Black
                            )

                            if (multa > 0) {
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    "⚠️ Multa aplicada: €100 — Guarda-chuva não devolvido após 24h!",
                                    color = Color.Red,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Text(
                                "💰 Total: €${"%.2f".format(totalCost)}",
                                color = Color.Black,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
