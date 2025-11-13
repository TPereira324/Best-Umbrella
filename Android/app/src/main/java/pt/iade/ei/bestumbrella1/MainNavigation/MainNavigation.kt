package pt.iade.ei.bestumbrella1.MainNavigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import pt.iade.ei.bestumbrella1.di.AppModule
import pt.iade.ei.bestumbrella1.views.LoginScreen
import pt.iade.ei.bestumbrella1.views.RegisterScreen
import pt.iade.ei.bestumbrella1.views.MapScreenWithMarkers
import pt.iade.ei.bestumbrella1.views.QrScannerScreen
import pt.iade.ei.bestumbrella1.views.HistoryScreen
import pt.iade.ei.bestumbrella1.views.ProfileScreen
import pt.iade.ei.bestumbrella1.views.PaymentScreen
import pt.iade.ei.bestumbrella1.views.RentalDetailsScreen
import pt.iade.ei.bestumbrella1.views.UsersAdminScreen
import pt.iade.ei.bestumbrella1.views.WeatherScreen

@Composable
fun MainNavigation(navController: NavHostController) {
    val context = LocalContext.current
    val sessionManager = remember { AppModule.provideSessionManager(context) }
    var startDestination by remember { mutableStateOf("login") }
    var isCheckingLogin by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val isLoggedIn = sessionManager.isLoggedIn()
        startDestination = if (isLoggedIn) "map" else "login"
        isCheckingLogin = false
    }

    if (!isCheckingLogin) {
        NavHost(
            navController = navController,
            startDestination = startDestination
        ) {
        composable("login") {
            LoginScreen(
                navController = navController,
                onLoginSuccess = {
                    navController.navigate("map") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("register") {
            RegisterScreen(
                navController = navController,
                onRegisterSuccess = {
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            )
        }

        composable("map") { MapScreenWithMarkers(navController) }
        composable("qrscanner") {
            QrScannerScreen(
                navController = navController,
                onCodeScanned = { code -> navController.navigate("rentalDetails/$code") }
            )
        }
        
        composable("history") { HistoryScreen(navController) }
        composable("profile") { ProfileScreen(navController) }
        composable("adminUsers") { UsersAdminScreen(navController) }
        composable("weather") { WeatherScreen(navController) }
        composable("payment") { PaymentScreen(navController, qrCode = "") }
        composable("payment/{qrCode}") { backStackEntry ->
            val qrCode = backStackEntry.arguments?.getString("qrCode") ?: ""
            PaymentScreen(navController, qrCode)
        }
        composable("payment/{qrCode}/{amount}") { backStackEntry ->
            val qrCode = backStackEntry.arguments?.getString("qrCode") ?: ""
            // Atualmente o PaymentScreen não recebe amount; valor será inserido manualmente.
            PaymentScreen(navController, qrCode)
        }
        composable("rentalDetails/{qrCode}") { backStackEntry ->
            val qrCode = backStackEntry.arguments?.getString("qrCode") ?: ""
            RentalDetailsScreen(navController, qrCode)
        }
        }
    }
}