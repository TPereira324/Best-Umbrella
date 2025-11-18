package pt.iade.ei.bestumbrella1.views

import android.annotation.SuppressLint
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import pt.iade.ei.bestumbrella1.BuildConfig

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(navController: NavController, qrCode: String) {
    var balance by remember { mutableStateOf(0.00) }
    var amountText by remember { mutableStateOf(TextFieldValue("")) }
    var showCheckout by remember { mutableStateOf(false) }
    var paymentMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pagamento", color = Color.Black, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("map") }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.Black)
                    }
                }
            )
        },
        bottomBar = { AppBottomNavigationBar(navController) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF2196F3).copy(alpha = 0.8f),
                            Color(0xFFE3F2FD)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    "Pagamento via PayPal",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFBBDEFB))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Saldo atual:", style = MaterialTheme.typography.bodyMedium, color = Color.Black, fontWeight = FontWeight.Bold)
                        Text(
                            "€${"%.2f".format(balance)}",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(24.dp))

                        OutlinedTextField(
                            value = amountText,
                            onValueChange = { amountText = it },
                            label = { Text("Valor a pagar (€)", color = Color.Black, fontWeight = FontWeight.Bold) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(16.dp))

                        Button(
                            onClick = {
                                val value = amountText.text.toDoubleOrNull()
                                if (value != null && value > 0) {
                                    showCheckout = true
                                    paymentMessage = null
                                } else {
                                    paymentMessage = "Insira um valor válido para pagar."
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF003087)
                            )
                        ) {
                            Icon(Icons.Default.Payment, contentDescription = null, tint = Color.White)
                            Spacer(Modifier.width(8.dp))
                            Text("Pagar com PayPal", color = Color.White, fontWeight = FontWeight.Bold)
                        }

                        Spacer(Modifier.height(16.dp))

                        if (paymentMessage != null) {
                            Text(
                                paymentMessage!!,
                                color = if (paymentMessage!!.contains("sucesso", ignoreCase = true)) Color(0xFF4CAF50) else MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (showCheckout) {
                            Spacer(Modifier.height(16.dp))
                            PayPalCheckoutWebView(
                                amount = amountText.text.toDoubleOrNull() ?: 0.0,
                                onResult = { result ->
                                    when (result.status) {
                                        "success" -> {
                                            paymentMessage = "Pagamento efetuado com sucesso via PayPal!"
                                            showCheckout = false
                                            val value = amountText.text.toDoubleOrNull()
                                            if (value != null) balance -= value
                                            amountText = TextFieldValue("")

                                            val goTo = if (qrCode.isBlank()) "profile" else "map"
                                            navController.navigate(goTo) {
                                                popUpTo(goTo) { inclusive = false }
                                                launchSingleTop = true
                                            }
                                        }
                                        "error" -> {
                                            val msg = result.message ?: "desconhecido"
                                            paymentMessage = "Erro no pagamento: ${msg}"
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

private data class PayPalResult(val status: String, val orderID: String? = null, val message: String? = null)

@SuppressLint("SetJavaScriptEnabled")
@Composable
private fun PayPalCheckoutWebView(amount: Double, onResult: (PayPalResult) -> Unit) {
    val html = remember(amount) {
        val valueStr = String.format("%.2f", amount)
        """
        <html>
        <head>
          <meta name=viewport content="width=device-width, initial-scale=1" />
          <script src="https://www.paypal.com/sdk/js?client-id=${BuildConfig.PAYPAL_CLIENT_ID}&currency=EUR&disable-funding=card"></script>
          <style> body { font-family: sans-serif; margin: 0; padding: 16px; } </style>
        </head>
        <body>
          <div id="paypal-button-container"></div>
          <script>
            const amount = '${valueStr}';
            paypal.Buttons({
              style: { shape: 'pill', color: 'blue', layout: 'vertical', label: 'paypal' },
              createOrder: function(data, actions) {
                return actions.order.create({
                  purchase_units: [{ amount: { value: amount } }]
                });
              },
              onApprove: function(data, actions) {
                return actions.order.capture().then(function(details) {
                  PayPalAndroid.postMessage(JSON.stringify({ status: 'success', orderID: data.orderID }));
                });
              },
              onError: function(err) {
                PayPalAndroid.postMessage(JSON.stringify({ status: 'error', message: String(err) }));
              }
            }).render('#paypal-button-container');
          </script>
        </body>
        </html>
        """.trimIndent()
    }

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(420.dp),
        factory = { ctx ->
            WebView(ctx).apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                webChromeClient = WebChromeClient()
                addJavascriptInterface(object {
                    @JavascriptInterface
                    fun postMessage(message: String) {
                        try {
                            val json = org.json.JSONObject(message)
                            val status = json.optString("status")
                            val orderID = json.optString("orderID")
                            val msg = json.optString("message")
                            onResult(PayPalResult(status = status, orderID = orderID, message = msg))
                        } catch (e: Exception) {
                            onResult(PayPalResult(status = "error", message = e.message))
                        }
                    }
                }, "PayPalAndroid")
                loadDataWithBaseURL(
                    "https://www.paypal.com/",
                    html,
                    "text/html",
                    "utf-8",
                    null
                )
            }
        },
        update = { webView ->
            webView.loadDataWithBaseURL(
                "https://www.paypal.com/",
                html,
                "text/html",
                "utf-8",
                null
            )
        }
    )
}


