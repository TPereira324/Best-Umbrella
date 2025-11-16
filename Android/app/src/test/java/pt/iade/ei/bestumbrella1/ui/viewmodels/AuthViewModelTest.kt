package pt.iade.ei.bestumbrella1.ui.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import io.mockk.coVerify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pt.iade.ei.bestumbrella1.data.Repository
import pt.iade.ei.bestumbrella1.network.UserResponse
import pt.iade.ei.bestumbrella1.models.SessionManager
import pt.iade.ei.bestumbrella1.viewmodels.AuthViewModel
import pt.iade.ei.bestumbrella1.network.UserProfileResponse
import pt.iade.ei.bestumbrella1.network.UserPreferences

@ExperimentalCoroutinesApi
class AuthViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: AuthViewModel
    private val repository: Repository = mockk()
    private val sessionManager: SessionManager = mockk(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        viewModel = AuthViewModel(repository, sessionManager)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `login with valid credentials updates loginResult`() = runTest {
        // Arrange
        val email = "test@example.com"
        val password = "password123"
        val userResponse = UserResponse(
            id = "1",
            name = "Test User",
            email = email,
            token = "fake-token-12345",
            isSuccessful = true
        )
        coEvery { repository.loginUser(email, password) } returns Result.success(userResponse)
        
        // Act
        viewModel.login(email, password)
        
        // Assert
        assertEquals(true, viewModel.loginResult.value?.success)
        assertEquals("Test User", viewModel.loginResult.value?.userName)
        assertEquals("fake-token-12345", viewModel.loginResult.value?.token)
        assertEquals(false, viewModel.isLoading.value)
    }

    @Test
    fun `login with invalid credentials updates error`() = runTest {
        // Arrange
        val email = "test@example.com"
        val password = "wrong_password"
        val errorMessage = "Invalid credentials"
        
        coEvery { repository.loginUser(email, password) } returns Result.failure(Exception(errorMessage))
        
        // Act
        viewModel.login(email, password)
        
        // Assert
        assertEquals(false, viewModel.loginResult.value?.success)
        assertEquals(errorMessage, viewModel.loginResult.value?.message)
        assertEquals(false, viewModel.isLoading.value)
    }

    @Test
    fun `register with valid data updates registerResult`() = runTest {
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
        coEvery { repository.registerUser(name, email, password, null) } returns Result.success(userResponse)
        
        // Act
        viewModel.register(name, email, password, null)
        
        // Assert
        assertEquals(true, viewModel.registerResult.value?.success)
        assertEquals(name, viewModel.registerResult.value?.userName)
        assertEquals(false, viewModel.isLoading.value)
    }

    @Test
    fun `getUserProfile with valid token updates userProfile`() = runTest {
        // Arrange
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
        
        coEvery { repository.getUserProfile() } returns Result.success(userProfileResponse)
        
        // Act
        viewModel.getUserProfile()
        
        // Assert
        coVerify { sessionManager.saveName("Test User") }
        coVerify { sessionManager.saveEmail("test@example.com") }
        assertEquals(false, viewModel.isLoading.value)
    }
}