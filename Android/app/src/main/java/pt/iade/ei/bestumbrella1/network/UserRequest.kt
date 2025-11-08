package pt.iade.ei.bestumbrella1.network

import com.google.gson.annotations.SerializedName

data class UserRequest(
    @SerializedName("nome") val name: String? = null,
    val email: String? = null,
    val password: String? = null,
    @SerializedName("telefone") val phone: String? = null
)