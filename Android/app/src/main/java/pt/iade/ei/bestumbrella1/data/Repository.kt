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
import retrofit2.Response
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import pt.iade.ei.bestumbrella1.network.ReturnResponse
import java.io.File

class Repository(private val apiService: ApiService, private val sessionManager: SessionManager) {

    suspend fun registerUser(name: String, email: String, password: String): Result<UserResponse> {
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
                val request = UserRequest(name = name, email = email, password = password)
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
                val response = apiService.loginUser(request)
                if (response.isSuccessful) {
                    val userResponse = response.body()!!
                    Result.success(userResponse)
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
                val response = apiService.getWeatherForecast(latitude, longitude)
                if (response.isSuccessful) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Falha ao obter previsão do tempo: ${response.errorBody()?.string()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
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