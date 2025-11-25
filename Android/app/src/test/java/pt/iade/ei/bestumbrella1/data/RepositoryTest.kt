package pt.iade.ei.bestumbrella1.data

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import pt.iade.ei.bestumbrella1.model.SessionManager
import pt.iade.ei.bestumbrella1.network.ApiService
import pt.iade.ei.bestumbrella1.network.UserRequest
import pt.iade.ei.bestumbrella1.network.UserResponse
import pt.iade.ei.bestumbrella1.network.UserProfileResponse
import pt.iade.ei.bestumbrella1.network.UserPreferences
import retrofit2.Response

@ExperimentalCoroutinesApi
class RepositoryTest {

    private lateinit var repository: Repository
    private val apiService: ApiService = mockk()
    private val sessionManager: SessionManager = mockk()

    @Before
    fun setup() {
        repository = Repository(apiService, sessionManager)
    }

    @Test
    fun `loginUser with valid credentials returns success`() = runTest {
        // Arrange
        val email = "test@example.com"
        val password = "password123"
        val userRequest = UserRequest(email = email, password = password)
        val userResponse = UserResponse(
            id = "1",
            name = "Test User",
            email = email,
            token = "fake-token-12345",
            isSuccessful = true
        )
        
        coEvery { apiService.loginAuth(any()) } returns Response.success(userResponse)
        
        // Act
        val result = repository.loginUser(email, password)
        
        // Assert
        assertTrue(result.isSuccess)
        assertEquals(userResponse, result.getOrNull())
    }

    @Test
    fun `registerUser with valid data returns success`() = runTest {
        // Arrange
        val name = "Test User"
        val email = "test@example.com"
        val password = "password123"
        val userResponse = UserResponse(
            id = "1",
            name = name,
            email = email,
            token = "fake-token-12345",
            isSuccessful = true
        )
        
        coEvery { apiService.registerUser(any()) } returns Response.success(userResponse)
        
        // Act
        val result = repository.registerUser(name, email, password, null)
        
        // Assert
        assertTrue(result.isSuccess)
        assertEquals(userResponse, result.getOrNull())
    }

    

    @Test
    fun `getUserProfile with valid token returns user profile`() = runTest {
        // Arrange
        val token = "valid_token"
        val userProfileResponse = UserProfileResponse(
            id = "1",
            name = "Test User",
            email = "test@example.com",
            preferences = UserPreferences(
                notificationsEnabled = true,
                locationTracking = true,
                weatherAlerts = true
            )
        )
        
        coEvery { sessionManager.getAuthToken() } returns token
        coEvery { apiService.getUserProfile("Bearer $token") } returns Response.success(userProfileResponse)
        
        // Act
        val result = repository.getUserProfile()
        
        // Assert
        assertTrue(result.isSuccess)
        assertEquals(userProfileResponse, result.getOrNull())
    }
}