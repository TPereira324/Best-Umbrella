package pt.iade.ei.bestumbrella1.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import pt.iade.ei.bestumbrella1.model.Station
import java.util.Locale

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
