package com.adyen.android.assignment.places.presentation.viewmodel

import com.adyen.android.assignment.domain.model.NearbyPlace
import com.adyen.android.assignment.domain.model.NearbyPlaceLocation
import com.adyen.android.assignment.domain.usecase.GetNearbyPlacesUseCase
import com.adyen.android.assignment.presentation.NearbyPlacesState
import com.adyen.android.assignment.presentation.viewmodel.NearbyPlacesViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

@ExperimentalCoroutinesApi
class NearbyPlacesViewModelTest {

    private lateinit var viewModel: NearbyPlacesViewModel
    private val getNearbyPlacesUseCase: GetNearbyPlacesUseCase = mockk()
    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = NearbyPlacesViewModel(getNearbyPlacesUseCase)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchNearbyPlaces updates state to Success when use case returns data`() = runTest {
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

        coEvery { getNearbyPlacesUseCase(any(), any()) } returns flowOf(fakePlaces)

        viewModel.fetchNearbyPlaces(12.34, 56.78)
        advanceUntilIdle()

        assertTrue(viewModel.state.value is NearbyPlacesState.Success)
        assertEquals(fakePlaces, (viewModel.state.value as NearbyPlacesState.Success).nearbyPlaces)
    }

    @Test
    fun `fetchNearbyPlaces updates state to Error on failure`() = runTest {
        coEvery { getNearbyPlacesUseCase(any(), any()) } throws Exception("API Failure")

        viewModel.fetchNearbyPlaces(12.34, 56.78)
        advanceUntilIdle()

        assertTrue(viewModel.state.value is NearbyPlacesState.Error)
        assertEquals("API Failure", (viewModel.state.value as NearbyPlacesState.Error).message)
    }
}
