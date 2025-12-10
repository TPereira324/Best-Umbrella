package pt.iade.ei.bestumbrella1.data

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pt.iade.ei.bestumbrella1.model.SessionManager
import pt.iade.ei.bestumbrella1.network.ApiService
import pt.iade.ei.bestumbrella1.model.Daily
import pt.iade.ei.bestumbrella1.model.Hourly
import pt.iade.ei.bestumbrella1.model.OpenWeatherForecastResponse
import pt.iade.ei.bestumbrella1.model.OpenWeatherOneCallResponse
import pt.iade.ei.bestumbrella1.network.UserProfileResponse
import pt.iade.ei.bestumbrella1.network.UserRequest
import pt.iade.ei.bestumbrella1.network.UserResponse
import pt.iade.ei.bestumbrella1.network.WeatherResponse

class Repository(private val apiService: ApiService, private val sessionManager: SessionManager) {
    private val weatherRepo = WeatherRepository()

    suspend fun registerUser(
        name: String,
        email: String,
        password: String,
        phone: String?
    ): Result<UserResponse> {
        return withContext(Dispatchers.IO) {
            try {
                if (email.equals("admin@bestumbrella", ignoreCase = true)) {
                    if (password == "admin123") {
                        val adminResponse = UserResponse(
                            id = "admin",
                            name = name.ifBlank { "Administrador" },
                            email = email,
                            token = "local-admin-token",
                            isSuccessful = true
                        )
                        return@withContext Result.success(adminResponse)
                    } else {
                        return@withContext Result.failure(Exception("Senha do administrador inválida"))
                    }
                }
                val request =
                    UserRequest(name = name, email = email, password = password, phone = phone)
                val response = apiService.registerUser(request)
                if (response.isSuccessful) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(
                        Exception(
                            "Falha no registro: ${
                                response.errorBody()?.string()
                            }"
                        )
                    )
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun getOneCallForecast(
        latitude: Double,
        longitude: Double
    ): Result<OpenWeatherOneCallResponse> {
        return weatherRepo.getOneCallForecast(latitude, longitude)
    }

    suspend fun getFiveDayForecast(
        latitude: Double,
        longitude: Double
    ): Result<OpenWeatherForecastResponse> {
        return weatherRepo.getFiveDayForecast(latitude, longitude)
    }

    fun mapForecastToHourlyDaily(forecast: OpenWeatherForecastResponse): Pair<List<Hourly>, List<Daily>> {
        return weatherRepo.mapForecastToHourlyDaily(forecast)
    }


    suspend fun loginUser(email: String, password: String): Result<UserResponse> {
        return withContext(Dispatchers.IO) {
            try {
                if (email.equals("admin@bestumbrella", ignoreCase = true)) {
                    if (password == "admin123") {
                        val adminResponse = UserResponse(
                            id = "admin",
                            name = "Administrador",
                            email = email,
                            token = "local-admin-token",
                            isSuccessful = true
                        )
                        return@withContext Result.success(adminResponse)
                    } else {
                        return@withContext Result.failure(Exception("Senha do administrador inválida"))
                    }
                }
                val request = UserRequest(email = email, password = password)
                val response = apiService.loginAuth(request)
                if (response.isSuccessful) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Falha no login: ${response.errorBody()?.string()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun getWeatherForecast(latitude: Double, longitude: Double): Result<WeatherResponse> {
        return weatherRepo.getWeatherForecast(latitude, longitude)
    }


    suspend fun getUserProfile(): Result<UserProfileResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val token = sessionManager.getAuthToken()
                if (token.isNullOrEmpty()) {
                    return@withContext Result.failure(Exception("Usuário não autenticado"))
                }

                val response = apiService.getUserProfile("Bearer $token")
                if (response.isSuccessful) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(
                        Exception(
                            "Falha ao obter perfil: ${
                                response.errorBody()?.string()
                            }"
                        )
                    )
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun resolveQrCode(input: String): String {
        val s = input.trim()
        val qIdx = s.indexOf('?')
        if (s.startsWith("bumb://")) {
            val query = if (qIdx >= 0) s.substring(qIdx + 1) else ""
            for (part in query.split('&')) {
                val eq = part.indexOf('=')
                val key = if (eq >= 0) part.substring(0, eq) else part
                val valStr = if (eq >= 0) part.substring(eq + 1) else ""
                if (key.equals("code", ignoreCase = true)) {
                    return java.net.URLDecoder.decode(valStr, Charsets.UTF_8)
                }
            }
            return ""
        }
        if (s.startsWith("http://") || s.startsWith("https://")) {
            val query = if (qIdx >= 0) s.substring(qIdx + 1) else ""
            for (part in query.split('&')) {
                val eq = part.indexOf('=')
                val key = if (eq >= 0) part.substring(0, eq) else part
                val valStr = if (eq >= 0) part.substring(eq + 1) else ""
                if (key.equals("code", ignoreCase = true)) {
                    return java.net.URLDecoder.decode(valStr, Charsets.UTF_8)
                }
            }
        }
        return s
    }

}