package pt.iade.ei.bestumbrella1.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("users/register")
    suspend fun registerUser(@Body request: UserRequest): Response<UserResponse>


    @POST("auth/login")
    suspend fun loginAuth(@Body request: UserRequest): Response<UserResponse>

    @GET("users/profile")
    suspend fun getUserProfile(@Header("Authorization") token: String): Response<UserProfileResponse>


}


