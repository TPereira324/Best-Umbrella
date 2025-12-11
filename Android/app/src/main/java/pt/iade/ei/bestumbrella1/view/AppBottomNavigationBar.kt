package pt.iade.ei.bestumbrella1.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import pt.iade.ei.bestumbrella1.di.AppModule
import pt.iade.ei.bestumbrella1.model.SessionManager

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScreenScaffold(
    navController: NavController,
    topAlpha: Float = 0.7f,
    content: @Composable BoxScope.() -> Unit
) {
    Scaffold(bottomBar = { AppBottomNavigationBar(navController) }) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF2196F3).copy(alpha = topAlpha),
                            Color(0xFFE3F2FD)
                        )
                    )
                )
        ) {
            content()
        }
    }
}

@Composable
fun AppGradientBackground(
    topAlpha: Float = 0.7f,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF2196F3).copy(alpha = topAlpha),
                        Color(0xFFE3F2FD)
                    )
                )
            )
    ) {
        content()
    }
}

@Composable
fun rememberSessionManager(): SessionManager {
    val context = LocalContext.current
    return remember { AppModule.provideSessionManager(context) }
}

fun NavController.navigateToMapFocus(stationName: String) {
    val encoded = android.net.Uri.encode(stationName)
    this.navigate("map/$encoded")
}

@Composable
fun rememberStationsRepository(): pt.iade.ei.bestumbrella1.data.StationsRepository {
    return remember { AppModule.provideStationsRepository() }
}
