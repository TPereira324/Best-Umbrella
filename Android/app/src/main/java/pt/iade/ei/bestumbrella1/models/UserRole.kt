package pt.iade.ei.bestumbrella1.models

enum class UserRole {
    USER,
    ADMIN;

    companion object {
        fun fromEmail(email: String?): UserRole {
            return if (email?.equals("admin@bestumbrella", ignoreCase = true) == true) {
                ADMIN
            } else {
                USER
            }
        }
    }
}