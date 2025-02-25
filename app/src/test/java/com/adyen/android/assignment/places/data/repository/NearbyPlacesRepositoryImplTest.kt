package com.adyen.android.assignment.places.data.repository

import com.adyen.android.assignment.data.datasource.api.model.Result
import com.adyen.android.assignment.data.datasource.remote.NearbyPlacesRemoteDataSource
import com.adyen.android.assignment.data.mapper.NearbyPlacesMapper
import com.adyen.android.assignment.data.repository.NearbyPlacesRepositoryImpl
import com.adyen.android.assignment.domain.model.NearbyPlace
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class NearbyPlacesRepositoryImplTest {

    private lateinit var repository: NearbyPlacesRepositoryImpl
    private val remoteDataSource: NearbyPlacesRemoteDataSource = mockk()
    private val mapper: NearbyPlacesMapper = mockk()

    @BeforeEach
    fun setup() {
        repository = NearbyPlacesRepositoryImpl(remoteDataSource, mapper)
    }

    @Test
    fun `getNearbyPlaces should return mapped domain data on success`() = runTest {
        val apiResults = listOf<Result>(mockk(relaxed = true))
        val mappedResults = listOf(
            NearbyPlace(
                name = "Library",
                distance = 500,
                categories = listOf("Cafe"),
                location = mockk(),
                latitude = 12.34,
                longitude = 56.78,
                timezone = "America/New_York"
            )
        )

        coEvery { remoteDataSource.getNearbyPlaces(any(), any()) } returns apiResults
        coEvery { mapper.mapList(apiResults) } returns mappedResults

        val result = repository.getNearbyPlaces(12.34, 56.78).first()

        assertEquals(mappedResults, result)
    }

    @Test
    fun `getNearbyPlaces should return empty list when remote source returns empty`() = runTest {
        coEvery { remoteDataSource.getNearbyPlaces(any(), any()) } returns emptyList()
        coEvery { mapper.mapList(emptyList()) } returns emptyList()

        val result = repository.getNearbyPlaces(12.34, 56.78).first()

        assertEquals(emptyList<NearbyPlace>(), result)
    }
}