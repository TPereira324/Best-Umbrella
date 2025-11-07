package pt.iade.ei.bestumbrella1.network

import com.google.gson.annotations.SerializedName

data class UserResponse(
    val id: String,
    @SerializedName(value = "name", alternate = ["nome"]) val name: String,
    val email: String,
    val token: String? = null,
    val isSuccessful: Boolean = true
)