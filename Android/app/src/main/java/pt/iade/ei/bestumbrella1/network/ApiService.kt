package pt.iade.ei.bestumbrella1.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query
import retrofit2.http.Part
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Field
import okhttp3.MultipartBody
import okhttp3.RequestBody
import pt.iade.ei.bestumbrella1.models.AdviceResponse

interface ApiService {
    @POST("users/register")
    suspend fun registerUser(@Body request: UserRequest): Response<UserResponse>

    @POST("users/login")
    suspend fun loginUser(@Body request: UserRequest): Response<UserResponse>

    
    @POST("auth/login")
    suspend fun loginAuth(@Body request: UserRequest): Response<UserResponse>

    @FormUrlEncoded
    @POST("login")
    suspend fun loginForm(
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<UserResponse>

    @GET("weather/forecast")
    suspend fun getWeatherForecast(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double
    ): Response<WeatherResponse>
 
    @GET("users/profile")
    suspend fun getUserProfile(@Header("Authorization") token: String): Response<UserProfileResponse>

    @PUT("users/profile")
    suspend fun updateUserProfile(
        @Header("Authorization") token: String,
        @Body request: UpdateProfileRequest
    ): Response<UserResponse>

    @Multipart
    @POST("returns")
    suspend fun submitReturn(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part,
        @Part("umbrellaId") umbrellaId: RequestBody,
        @Part("notes") notes: RequestBody
    ): Response<ReturnResponse>

    @GET("advice")
    suspend fun getAdvice(): Response<AdviceResponse>

    @GET("users")
    suspend fun getAllUsers(@Header("Authorization") token: String): Response<List<UserProfileResponse>>
}


