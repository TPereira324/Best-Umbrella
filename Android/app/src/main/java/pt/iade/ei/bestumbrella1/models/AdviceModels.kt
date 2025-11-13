package pt.iade.ei.bestumbrella1.models

import com.google.gson.annotations.SerializedName

data class AdviceResponse(
    val slip: Slip
)

data class Slip(
    val advice: String,
    @SerializedName("slip_id") val slipId: String
)

