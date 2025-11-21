package pt.iade.ei.bestumbrella1.views.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import java.util.Locale

@Composable
fun FilterBar(
    stations: List<Station>,
    center: LatLng,
    current: StationFilter,
    onChange: (StationFilter) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFBBDEFB))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        val totalCount = stations.size
        val availableCount = stations.count { it.available > 0 }
        val nearbyCount = remember(stations, center) {
            stations.sortedBy { distanceKm(it.location, center) }.take(5).size
        }

        FilterChip(
            selected = current == StationFilter.ALL,
            onClick = { onChange(StationFilter.ALL) },
            label = {
                Text(
                    "Todas ($totalCount)",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }
        )
        FilterChip(
            selected = current == StationFilter.AVAILABLE,
            onClick = { onChange(StationFilter.AVAILABLE) },
            label = {
                Text(
                    "Disponíveis ($availableCount)",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }
        )
        FilterChip(
            selected = current == StationFilter.NEARBY,
            onClick = { onChange(StationFilter.NEARBY) },
            label = {
                Text(
                    "Próximas ($nearbyCount)",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }
        )
    }
}

@Composable
fun StationBottomSheet(station: Station) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                station.name,
                style = MaterialTheme.typography.titleLarge,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
            AssistChip(
                onClick = {},
                label = {
                    Text(
                        "${station.available} disponíveis",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                })
        }

        Spacer(Modifier.padding(12.dp))
        Text(
            text = String.format(Locale.US, "\uD83D\uDCCD %.1f km de distância", 0.3),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black
        )

        Spacer(Modifier.padding(16.dp))
        Text(
            "Informações da Estação",
            style = MaterialTheme.typography.titleMedium,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.padding(8.dp))
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("☂ Guarda-chuvas", color = Color.Black, fontWeight = FontWeight.Bold)
                    Text("${station.available} de ${station.total}", color = Color.Black)
                }
                Spacer(Modifier.padding(8.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("⏱ Tempo máximo", color = Color.Black, fontWeight = FontWeight.Bold)
                    Text("24 horas", color = Color.Black)
                }
                Spacer(Modifier.padding(8.dp))
                Text(
                    "⚠️ Multa aplicada após 24h",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.padding(8.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("€ Tarifa", color = Color.Black, fontWeight = FontWeight.Bold)
                    Text("€0.50/hora", color = Color.Black)
                }
            }
        }
    }
}