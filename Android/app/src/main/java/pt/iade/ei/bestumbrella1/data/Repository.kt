package pt.iade.ei.bestumbrella1.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pt.iade.ei.bestumbrella1.models.SessionManager
import pt.iade.ei.bestumbrella1.network.ApiService
import pt.iade.ei.bestumbrella1.network.UpdateProfileRequest
import pt.iade.ei.bestumbrella1.network.UserPreferences
import pt.iade.ei.bestumbrella1.network.UserProfileResponse
import pt.iade.ei.bestumbrella1.network.UserRequest
import pt.iade.ei.bestumbrella1.network.UserResponse
import pt.iade.ei.bestumbrella1.network.WeatherResponse
import pt.iade.ei.bestumbrella1.BuildConfig
import pt.iade.ei.bestumbrella1.network.OpenWeatherRetrofit
import pt.iade.ei.bestumbrella1.network.OpenWeatherCurrentResponse
import pt.iade.ei.bestumbrella1.network.OpenWeatherOneCallResponse
import pt.iade.ei.bestumbrella1.network.OpenWeatherForecastResponse
import pt.iade.ei.bestumbrella1.network.ForecastItem
import pt.iade.ei.bestumbrella1.network.Hourly
import pt.iade.ei.bestumbrella1.network.Daily
import pt.iade.ei.bestumbrella1.network.DailyTemp
import pt.iade.ei.bestumbrella1.network.Weather
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import pt.iade.ei.bestumbrella1.network.ReturnResponse
import java.io.File

class Repository(private val apiService: ApiService, private val sessionManager: SessionManager) {

    suspend fun registerUser(name: String, email: String, password: String, phone: String?): Result<UserResponse> {
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
                val request = UserRequest(name = name, email = email, password = password, phone = phone)
                val response = apiService.registerUser(request)
                if (response.isSuccessful) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Falha no registro: ${response.errorBody()?.string()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun getOneCallForecast(latitude: Double, longitude: Double): Result<OpenWeatherOneCallResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = OpenWeatherRetrofit.api.getOneCall(
                    latitude = latitude,
                    longitude = longitude,
                    apiKey = BuildConfig.WEATHER_API_KEY,
                    units = "metric",
                    lang = "pt",
                    exclude = "minutely"
                )
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Falha ao obter previsão (One Call): HTTP ${response.code()} ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun getFiveDayForecast(latitude: Double, longitude: Double): Result<OpenWeatherForecastResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = OpenWeatherRetrofit.api.getFiveDayForecast(
                    latitude = latitude,
                    longitude = longitude,
                    apiKey = BuildConfig.WEATHER_API_KEY,
                    units = "metric",
                    lang = "pt"
                )
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Falha ao obter previsão (forecast 5 dias): HTTP ${response.code()} ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    // Conversão de forecast 3h -> listas Hourly (24h) e Daily (5 dias)
    fun mapForecastToHourlyDaily(forecast: OpenWeatherForecastResponse): Pair<List<Hourly>, List<Daily>> {
        val hourly = forecast.list.take(8 * 3) // 24h ~ 8 blocos de 3h; tomamos até 24h
            .take(8)
            .map { it.toHourly() }

        // Agrupar por dia (UTC) usando dt
        val byDay = forecast.list.groupBy { item ->
            // Dia baseado em epoch truncado a data
            (item.dt / 86400L)
        }
        val daily = byDay.entries.sortedBy { it.key }.take(5).map { (_, items) ->
            val temps = items.mapNotNull { it.main.temp }
            val min = items.mapNotNull { it.main.tempMin }.minOrNull() ?: temps.minOrNull() ?: 0.0
            val max = items.mapNotNull { it.main.tempMax }.maxOrNull() ?: temps.maxOrNull() ?: 0.0
            val dt = items.firstOrNull()?.dt ?: 0L
            val weather: List<Weather> = items.firstOrNull()?.weather ?: emptyList()
            val popAvg = items.mapNotNull { it.precipitationProbability }.average().takeIf { !it.isNaN() }
            Daily(
                dt = dt,
                sunrise = null, // não disponível por dia no endpoint forecast
                sunset = null,
                temp = DailyTemp(min = min, max = max),
                weather = weather,
                precipitationProbability = popAvg
            )
        }
        return Pair(hourly, daily)
    }

    private fun ForecastItem.toHourly(): Hourly {
        val pop = precipitationProbability
        return Hourly(
            dt = dt,
            temp = main.temp,
            weather = weather,
            precipitationProbability = pop
        )
    }

    suspend fun loginUser(email: String, password: String): Result<UserResponse> {
        return withContext(Dispatchers.IO) {
            try {
                // Fallback local para demonstração em aula: aceita admin sem backend
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
                // 1) Tenta /users/login
                var response = apiService.loginUser(request)
                // 2) Se 404, tenta /auth/login
                if (!response.isSuccessful && response.code() == 404) {
                    response = apiService.loginAuth(request)
                }
                // 3) Se continuar falhando (404/400/415), tenta /login como form-url-encoded (username/password)
                if (!response.isSuccessful && (response.code() == 404 || response.code() == 400 || response.code() == 415)) {
                    response = apiService.loginForm(username = email, password = password)
                }
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
        return withContext(Dispatchers.IO) {
            try {
                val response = OpenWeatherRetrofit.api.getCurrentWeather(
                    latitude = latitude,
                    longitude = longitude,
                    apiKey = BuildConfig.WEATHER_API_KEY,
                    units = "metric",
                    lang = "pt"
                )
                if (response.isSuccessful && response.body() != null) {
                    val mapped = mapOpenWeatherToWeatherResponse(response.body()!!)
                    Result.success(mapped)
                } else {
                    Result.failure(Exception("Falha ao obter clima em tempo real: HTTP ${response.code()} ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    private fun mapOpenWeatherToWeatherResponse(open: OpenWeatherCurrentResponse): WeatherResponse {
        val temp = open.main.temp
        val desc = open.weather.firstOrNull()?.description ?: ""
        val humidity = open.main.humidity
        val wind = open.wind.speed
        // A API atual não fornece probabilidade de chuva (pop) no endpoint de clima atual.
        // Mantemos 0.0 para não inventar valores.
        val rainProbability = 0.0
        return WeatherResponse(
            temperature = temp,
            description = desc,
            humidity = humidity,
            windSpeed = wind,
            rainProbability = rainProbability,
            isSuccessful = true
        )
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
                    Result.failure(Exception("Falha ao obter perfil: ${response.errorBody()?.string()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun updateUserProfile(name: String?, preferences: UserPreferences?): Result<UserResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val token = sessionManager.getAuthToken()
                if (token.isNullOrEmpty()) {
                    return@withContext Result.failure(Exception("Usuário não autenticado"))
                }
                
                val request = UpdateProfileRequest(name, preferences)
                val response = apiService.updateUserProfile("Bearer $token", request)
                if (response.isSuccessful) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Falha ao atualizar perfil: ${response.errorBody()?.string()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun getAllUsers(): Result<List<UserProfileResponse>> {
        return withContext(Dispatchers.IO) {
            try {
                val token = sessionManager.getAuthToken()
                if (token.isNullOrEmpty()) {
                    return@withContext Result.failure(Exception("Usuário não autenticado"))
                }

                val response = apiService.getAllUsers("Bearer $token")
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Falha ao obter utilizadores: ${response.errorBody()?.string()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun submitUmbrellaReturn(imageFile: File, umbrellaId: String, notes: String): Result<ReturnResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val token = sessionManager.getAuthToken()
                if (token.isNullOrEmpty()) {
                    return@withContext Result.failure(Exception("Usuário não autenticado"))
                }

                val imageRequestBody = imageFile.asRequestBody("image/jpeg".toMediaType())
                val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, imageRequestBody)
                val umbrellaIdBody: RequestBody = umbrellaId.toRequestBody("text/plain".toMediaType())
                val notesBody: RequestBody = notes.toRequestBody("text/plain".toMediaType())

                val response = apiService.submitReturn(
                    token = "Bearer $token",
                    image = imagePart,
                    umbrellaId = umbrellaIdBody,
                    notes = notesBody
                )

                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Falha ao submeter devolução: HTTP ${response.code()} ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

}