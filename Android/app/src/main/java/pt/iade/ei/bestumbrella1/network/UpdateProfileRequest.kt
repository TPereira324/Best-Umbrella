package pt.iade.ei.bestumbrella1.network

import com.google.gson.annotations.SerializedName

data class UpdateProfileRequest(
    @SerializedName("nome") val name: String? = null,
    val preferences: UserPreferences? = null,
    @SerializedName("telefone") val phone: String? = null
)