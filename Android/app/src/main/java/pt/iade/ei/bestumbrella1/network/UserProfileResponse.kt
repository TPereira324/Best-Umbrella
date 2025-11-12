package pt.iade.ei.bestumbrella1.network

import com.google.gson.annotations.SerializedName

data class UserProfileResponse(
    val id: String,
    @SerializedName(value = "name", alternate = ["nome"]) val name: String,
    val email: String,
    val preferences: UserPreferences? = null,
    val isSuccessful: Boolean = true
)

