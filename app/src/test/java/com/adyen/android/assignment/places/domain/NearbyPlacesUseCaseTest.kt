package com.adyen.android.assignment.places.domain

import com.adyen.android.assignment.data.repository.NearbyPlacesRepository
import com.adyen.android.assignment.domain.model.NearbyPlace
import com.adyen.android.assignment.domain.model.NearbyPlaceLocation
import com.adyen.android.assignment.domain.usecase.GetNearbyPlacesUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class GetNearbyPlacesUseCaseTest {

    private lateinit var useCase: GetNearbyPlacesUseCase
    private val repository: NearbyPlacesRepository = mockk()

    @BeforeEach
    fun setup() {
        useCase = GetNearbyPlacesUseCase(repository)
    }

    @Test
    fun `invoke should return nearby places from repository`() = runTest {
        val fakePlaces = listOf(
            NearbyPlace(
                name = "Central Park",
                distance = 120,
                categories = listOf("Park"),
                location = NearbyPlaceLocation(
                    address = "5th Ave",
                    country = "USA",
                    locality = "New York",
                    region = "NY",
                    postcode = "10001"
                ),
                latitude = 40.7128,
                longitude = -74.0060,
                timezone = "America/New_York"
            ),
            NearbyPlace(
                name = "Starbucks",
                distance = 50,
                categories = listOf("Cafe"),
                location = NearbyPlaceLocation(
                    address = "456 Avenue",
                    country = "USA",
                    locality = "Los Angeles",
                    region = "CA",
                    postcode = "90001"
                ),
                latitude = 34.0522,
                longitude = -118.2437,
                timezone = "America/Los_Angeles"
            )
        )

        coEvery { repository.getNearbyPlaces(any(), any()) } returns flowOf(fakePlaces)

        val result = useCase(12.34, 56.78)

        result.collect { places ->
            assertEquals(2, places.size)
            assertEquals("Central Park", places[0].name)
            assertEquals("Starbucks", places[1].name)
        }
    }
}