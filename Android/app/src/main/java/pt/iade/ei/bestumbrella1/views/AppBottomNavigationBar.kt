package pt.iade.ei.bestumbrella1.views

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun AppBottomNavigationBar(navController: NavController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    NavigationBar {
        NavigationBarItem(
            selected = currentRoute == "weather",
            onClick = { navController.navigate("weather") },
            icon = {
                androidx.compose.material3.Icon(
                    Icons.Default.Cloud,
                    contentDescription = null
                )
            },
            label = { Text("Tempo", color = Color.Black, fontWeight = FontWeight.Bold) }
        )
        NavigationBarItem(
            selected = currentRoute == "history",
            onClick = { navController.navigate("history") },
            icon = {
                androidx.compose.material3.Icon(
                    Icons.Default.History,
                    contentDescription = null
                )
            },
            label = { Text("Histórico", color = Color.Black, fontWeight = FontWeight.Bold) }
        )
        NavigationBarItem(
            selected = currentRoute == "map",
            onClick = { navController.navigate("map") },
            icon = {
                androidx.compose.material3.Icon(
                    Icons.Default.Map,
                    contentDescription = null
                )
            },
            label = { Text("Mapa", color = Color.Black, fontWeight = FontWeight.Bold) }
        )
        NavigationBarItem(
            selected = currentRoute == "qrscanner",
            onClick = { navController.navigate("qrscanner") },
            icon = {
                androidx.compose.material3.Icon(
                    Icons.Default.QrCodeScanner,
                    contentDescription = null
                )
            },
            label = { Text("Scanner", color = Color.Black, fontWeight = FontWeight.Bold) }
        )
        NavigationBarItem(
            selected = currentRoute == "profile",
            onClick = { navController.navigate("profile") },
            icon = {
                androidx.compose.material3.Icon(
                    Icons.Default.Person,
                    contentDescription = null
                )
            },
            label = { Text("Perfil", color = Color.Black, fontWeight = FontWeight.Bold) }
        )
    }
}