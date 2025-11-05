package pt.iade.ei.bestumbrella1.network

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import pt.iade.ei.bestumbrella1.data.Repository
import pt.iade.ei.bestumbrella1.models.SessionManager
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(AndroidJUnit4::class)
class ApiIntegrationTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: ApiService
    private lateinit var repository: Repository
    private lateinit var sessionManager: SessionManager

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        sessionManager = SessionManager(appContext)
        repository = Repository(apiService, sessionManager)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun testLoginUser() = runBlocking {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(
                """
                {
                    "id": "1",
                    "name": "Test User",
                    "email": "test@example.com",
                    "token": "fake-token-12345",
                    "isSuccessful": true
                }
                """.trimIndent()
            )
        mockWebServer.enqueue(mockResponse)

        val result = repository.loginUser("test@example.com", "password123")

        val request = mockWebServer.takeRequest()
        assertNotNull(request.path)
        assertTrue(request.path!!.startsWith("/users/login"))

        assertTrue(result.isSuccess)
        val userResponse = result.getOrNull()
        assertEquals(true, userResponse?.isSuccessful)
        assertEquals("Test User", userResponse?.name)
        assertEquals("fake-token-12345", userResponse?.token)
    }

    @Test
    fun testGetWeatherForecast() = runBlocking {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(
                """
                {
                    "temperature": 25.0,
                    "description": "Sunny",
                    "humidity": 70,
                    "windSpeed": 10.0,
                    "rainProbability": 0.0,
                    "isSuccessful": true
                }
                """.trimIndent()
            )
        mockWebServer.enqueue(mockResponse)

        val result = repository.getWeatherForecast(38.7223, -9.1393)

        val request = mockWebServer.takeRequest()
        assertNotNull(request.path)
        val path = request.path!!
        assertTrue(path.startsWith("/weather/forecast"))
        assertTrue(path.contains("lat=38.7223"))
        assertTrue(path.contains("lon=-9.1393"))

        assertTrue(result.isSuccess)
        val weatherResponse = result.getOrNull()
        assertNotNull(weatherResponse)
        val weather = weatherResponse!!
        assertEquals(25.0, weather.temperature, 0.0)
        assertEquals("Sunny", weather.description)
        assertEquals(70, weather.humidity)
        assertEquals(10.0, weather.windSpeed, 0.0)
        assertEquals(0.0, weather.rainProbability, 0.0)
    }
}