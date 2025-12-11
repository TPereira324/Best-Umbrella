package pt.iade.ei.bestumbrella1.controllers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pt.iade.ei.bestumbrella1.BuildConfig
import pt.iade.ei.bestumbrella1.model.SessionManager
import java.text.NumberFormat
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

    fun formatCurrency(amount: Double): String {
        return NumberFormat.getCurrencyInstance(Locale("pt", "PT")).format(amount)
    }

    fun priceForType(tipo: String?): Double {
        return when (tipo?.lowercase(Locale.ROOT)) {
            "automático" -> 3.49
            "compacto" -> 2.99
            "manual" -> 2.49
            else -> 2.99
        }
    }

    fun qrUrlFor(code: String, size: Int = 256): String {
        val base = pt.iade.ei.bestumbrella1.BuildConfig.API_BASE_URL.removeSuffix("/")
        val origin = base.removeSuffix("/api")
        return "$origin/api/guardachuvas/codigo/$code/qrcode?size=$size"
    }

    private val _rentalStartMs = MutableLiveData<Long?>()
    val rentalStartMs: LiveData<Long?> = _rentalStartMs

    private val _rentalQr = MutableLiveData<String?>()
    val rentalQr: LiveData<String?> = _rentalQr

    private val _elapsedMs = MutableLiveData<Long>(0L)
    val elapsedMs: LiveData<Long> = _elapsedMs

    private var timerJob: Job? = null

    fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                val start = _rentalStartMs.value
                if (start != null) {
                    _elapsedMs.value = System.currentTimeMillis() - start
                } else {
                    _elapsedMs.value = 0L
                }
                delay(1000)
            }
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    fun refreshRentalState() {
        viewModelScope.launch {
            _rentalStartMs.value = sessionManager.getRentalStartMs()
            _rentalQr.value = sessionManager.getRentalQrCode()
        }
    }

    fun startRental(qrCode: String) {
        viewModelScope.launch {
            sessionManager.startRental(qrCode)
            refreshRentalState()
        }
    }

    fun stopRental() {
        viewModelScope.launch {
            sessionManager.stopRental()
            refreshRentalState()
        }
    }
}
