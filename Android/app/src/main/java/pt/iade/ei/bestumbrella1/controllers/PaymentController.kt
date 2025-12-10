package pt.iade.ei.bestumbrella1.controllers

import androidx.lifecycle.ViewModel
import pt.iade.ei.bestumbrella1.BuildConfig
import pt.iade.ei.bestumbrella1.model.SessionManager
import java.util.Locale
import kotlin.math.round

class PaymentController(private val sessionManager: SessionManager) : ViewModel() {

    fun hasClientId(): Boolean = BuildConfig.PAYPAL_CLIENT_ID.isNotBlank()

    fun clientId(): String = BuildConfig.PAYPAL_CLIENT_ID

    fun apiBaseUrl(): String = BuildConfig.API_BASE_URL

    fun serverBaseUrl(): String = BuildConfig.API_BASE_URL.replace("api/", "")

    suspend fun onPayPalSuccess(action: String, qrCode: String) {
        when (action.lowercase(Locale.ROOT)) {
            "end" -> sessionManager.stopRental()
            else -> sessionManager.startRental(qrCode)
        }
    }

    fun formatError(name: String?, message: String?, debugId: String?): String {
        val title = name ?: "Erro"
        val msg = message ?: "desconhecido"
        val dbg = debugId?.let { " (debug_id: $it)" } ?: ""
        return "$title: $msg$dbg"
    }

    fun computeAmount(elapsedMs: Long): Double {
        val baseFee = 0.30
        val ratePerMin = 0.15
        val minutesRounded = (((elapsedMs + 59999L) / 60000L).toInt()).coerceAtLeast(1)
        val total = baseFee + minutesRounded * ratePerMin
        return round(total * 100) / 100.0
    }

    suspend fun currentRentalAmount(): Double? {
        val start = sessionManager.getRentalStartMs()
        return start?.let { computeAmount(System.currentTimeMillis() - it) }
    }
}

