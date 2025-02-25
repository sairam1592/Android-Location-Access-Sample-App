package com.adyen.android.assignment.places.data.remote

import com.adyen.android.assignment.data.datasource.api.PlacesService
import com.adyen.android.assignment.data.datasource.api.model.ResponseWrapper
import com.adyen.android.assignment.data.datasource.api.model.Result
import com.adyen.android.assignment.data.datasource.remote.NearbyPlacesRemoteDataSourceImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

class NearbyPlacesRemoteDataSourceImplTest {

    private lateinit var remoteDataSource: NearbyPlacesRemoteDataSourceImpl
    private val placesService: PlacesService = mockk()

    @BeforeEach
    fun setup() {
        remoteDataSource = NearbyPlacesRemoteDataSourceImpl(placesService)
    }

    @Test
    fun `getNearbyPlaces returns expected results`() = runTest {
        val fakeResults = listOf(
            Result(
                name = "Central Park",
                distance = 120,
                categories = emptyList(),
                location = mockk(relaxed = true),
                geocode = mockk(relaxed = true),
                timezone = "America/New_York"
            ),
            Result(
                name = "Starbucks",
                distance = 50,
                categories = emptyList(),
                location = mockk(relaxed = true),
                geocode = mockk(relaxed = true),
                timezone = "America/Los_Angeles"
            )
        )

        val fakeResponse = ResponseWrapper(results = fakeResults)

        coEvery { placesService.getVenueRecommendations(any()) } returns fakeResponse

        val result = remoteDataSource.getNearbyPlaces(12.34, 56.78)

        assertEquals(2, result.size)
        assertEquals("Central Park", result[0].name)
        assertEquals("Starbucks", result[1].name)

        coVerify { placesService.getVenueRecommendations(any()) }
    }

    @Test
    fun `getNearbyPlaces returns empty list on API failure`() = runTest {
        coEvery { placesService.getVenueRecommendations(any()) } throws Exception("Network Error")

        val result = remoteDataSource.getNearbyPlaces(12.34, 56.78)

        assertTrue(result.isEmpty())

        coVerify { placesService.getVenueRecommendations(any()) }
    }
}