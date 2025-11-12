package pt.iade.ei.bestumbrella1.network

data class UserPreferences(
    val notificationsEnabled: Boolean = true,
    val locationTracking: Boolean = true,
    val weatherAlerts: Boolean = true
)

