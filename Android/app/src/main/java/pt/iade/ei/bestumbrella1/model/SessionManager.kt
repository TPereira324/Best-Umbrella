package pt.iade.ei.bestumbrella1.model

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.internalDataStore by preferencesDataStore(name = "session")

class SessionManager(context: Context) {

    private val dataStore = context.internalDataStore

    companion object {
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val NAME_KEY = stringPreferencesKey("name")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val USER_ID_KEY = stringPreferencesKey("user_id")
        private val RENTAL_START_MS_KEY = stringPreferencesKey("rental_start_ms")
        private val RENTAL_QR_KEY = stringPreferencesKey("rental_qr")
    }

    suspend fun saveEmail(email: String) {
        saveValue(EMAIL_KEY, email)
    }

    suspend fun getEmail(): String? {
        return getValue(EMAIL_KEY)
    }

    suspend fun saveName(name: String) {
        saveValue(NAME_KEY, name)
    }

    suspend fun getName(): String? {
        return getValue(NAME_KEY)
    }

    suspend fun saveToken(token: String) {
        saveValue(TOKEN_KEY, token)
    }

    suspend fun getToken(): String? {
        return getValue(TOKEN_KEY)
    }

    suspend fun saveUserId(userId: String) {
        saveValue(USER_ID_KEY, userId)
    }

    suspend fun getUserId(): String? {
        return getValue(USER_ID_KEY)
    }

    suspend fun getAuthToken(): String? {
        return getToken()
    }

    suspend fun isLoggedIn(): Boolean {
        val token = getToken()
        return !token.isNullOrEmpty()
    }

    suspend fun clearSession() {
        dataStore.edit { it.clear() }
    }

    private suspend fun saveValue(key: Preferences.Key<String>, value: String) {
        dataStore.edit { prefs -> prefs[key] = value }
    }

    private suspend fun getValue(key: Preferences.Key<String>): String? {
        return dataStore.data.map { it[key] }.first()
    }

    suspend fun startRental(qrCode: String) {
        saveValue(RENTAL_QR_KEY, qrCode)
        saveValue(RENTAL_START_MS_KEY, System.currentTimeMillis().toString())
    }

    suspend fun stopRental() {
        dataStore.edit { prefs ->
            prefs.remove(RENTAL_QR_KEY)
            prefs.remove(RENTAL_START_MS_KEY)
        }
    }

    suspend fun getRentalStartMs(): Long? {
        val v = getValue(RENTAL_START_MS_KEY)
        return v?.toLongOrNull()
    }

    suspend fun getRentalQrCode(): String? {
        return getValue(RENTAL_QR_KEY)
    }

    suspend fun isRentalOngoing(): Boolean {
        val start = getRentalStartMs()
        val qr = getRentalQrCode()
        return start != null && !qr.isNullOrBlank()
    }

    suspend fun rentalElapsedMs(): Long? {
        val start = getRentalStartMs()
        return start?.let { System.currentTimeMillis() - it }
    }

}

