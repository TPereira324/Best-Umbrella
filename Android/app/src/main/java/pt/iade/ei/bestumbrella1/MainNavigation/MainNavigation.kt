package pt.iade.ei.bestumbrella1.MainNavigation

import android.os.Build
import androidx.annotation.RequiresApi
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
import pt.iade.ei.bestumbrella1.view.HistoryScreen
import pt.iade.ei.bestumbrella1.view.LoginScreen
import pt.iade.ei.bestumbrella1.view.MapScreenWithMarkers
import pt.iade.ei.bestumbrella1.view.PaymentScreen
import pt.iade.ei.bestumbrella1.view.ProfileScreen
import pt.iade.ei.bestumbrella1.view.QrScannerScreen
import pt.iade.ei.bestumbrella1.view.RegisterScreen
import pt.iade.ei.bestumbrella1.view.RentalDetailsScreen
import pt.iade.ei.bestumbrella1.view.WeatherScreen

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
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
            composable("map/{station}") { backStackEntry ->
                val station = backStackEntry.arguments?.getString("station")
                MapScreenWithMarkers(navController, focusStation = station)
            }
            composable("qrscanner") {
                QrScannerScreen(
                    navController = navController,
                    onCodeScanned = { code -> navController.navigate("rentalDetails/$code") }
                )
            }
            composable("qrscannerMap") {
                QrScannerScreen(
                    navController = navController,
                    onCodeScanned = { _ -> navController.navigate("map") }
                )
            }

            composable("history") { HistoryScreen(navController) }
            composable("profile") { ProfileScreen(navController) }
            composable("weather") { WeatherScreen(navController) }
            composable("payment") { PaymentScreen(navController, qrCode = "") }
            composable("paymentMap") { PaymentScreen(navController, qrCode = "MAP") }
            composable("payment/{qrCode}") { backStackEntry ->
                val qrCode = backStackEntry.arguments?.getString("qrCode") ?: ""
                PaymentScreen(navController, qrCode)
            }
            composable("payment/{qrCode}/{amount}") { backStackEntry ->
                val qrCode = backStackEntry.arguments?.getString("qrCode") ?: ""
                val amount = backStackEntry.arguments?.getString("amount")?.toDoubleOrNull()
                PaymentScreen(navController, qrCode, amount)
            }
            composable("paypalCheckout/{amount}/{qrCode}") { backStackEntry ->
                val amount = backStackEntry.arguments?.getString("amount")?.toDoubleOrNull() ?: 0.0
                val qrCode = backStackEntry.arguments?.getString("qrCode") ?: ""
                pt.iade.ei.bestumbrella1.view.PayPalCheckoutScreen(navController, amount, qrCode)
            }
            composable("paypalCheckout/{amount}/{qrCode}/{action}") { backStackEntry ->
                val amount = backStackEntry.arguments?.getString("amount")?.toDoubleOrNull() ?: 0.0
                val qrCode = backStackEntry.arguments?.getString("qrCode") ?: ""
                val action = backStackEntry.arguments?.getString("action") ?: "start"
                pt.iade.ei.bestumbrella1.view.PayPalCheckoutScreen(
                    navController,
                    amount,
                    qrCode,
                    action
                )
            }
            composable("rentalDetails/{qrCode}") { backStackEntry ->
                val qrCode = backStackEntry.arguments?.getString("qrCode") ?: ""
                RentalDetailsScreen(navController, qrCode)
            }
        }
    }
}
