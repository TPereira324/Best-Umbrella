package pt.iade.ei.bestumbrella1.views

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController

@Composable
fun MainBottomBar(navController: NavController) {
    NavigationBar(containerColor = Color.White, contentColor = Color(0xFF1976D2)) {
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("weather") },
            icon = { Icon(Icons.Default.Cloud, contentDescription = "Tempo", tint = Color.Black) },
            label = { Text("Tempo", color = Color.Black, fontWeight = FontWeight.Bold) }
        )
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("history") },
            icon = { Icon(Icons.Default.History, contentDescription = "Histórico", tint = Color.Black) },
            label = { Text("Histórico", color = Color.Black, fontWeight = FontWeight.Bold) }
        )
        NavigationBarItem(
            selected = true,
            onClick = {},
            icon = { Icon(Icons.Default.Map, contentDescription = "Mapa", tint = Color.Black) },
            label = { Text("Mapa", color = Color.Black, fontWeight = FontWeight.Bold) }
        )
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("qrscanner") },
            icon = { Icon(Icons.Default.QrCodeScanner, contentDescription = "Scanner", tint = Color.Black) },
            label = { Text("Scanner", color = Color.Black, fontWeight = FontWeight.Bold) }
        )
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("profile") },
            icon = { Icon(Icons.Default.Person, contentDescription = "Perfil", tint = Color.Black) },
            label = { Text("Perfil", color = Color.Black, fontWeight = FontWeight.Bold) }
        )
    }
}