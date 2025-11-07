package pt.iade.ei.bestumbrella1.network

import pt.iade.ei.bestumbrella1.models.AdviceResponse
import retrofit2.http.GET

interface AdviceApiService {
    @GET("advice")
    suspend fun getAdvice(): AdviceResponse
}