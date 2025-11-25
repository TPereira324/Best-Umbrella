package pt.iade.ei.bestumbrella1.model

data class AuthResponse(
    val success: Boolean,
    val message: String,
    val userId: String? = null,
    val userName: String? = null,
    val userEmail: String? = null,
    val token: String? = null
)

