package pt.iade.ei.bestumbrella1.network

data class ReturnResponse(
    val success: Boolean,
    val message: String?,
    val returnId: Long?
)