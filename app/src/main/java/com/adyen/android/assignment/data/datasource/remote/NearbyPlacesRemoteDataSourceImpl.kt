package com.adyen.android.assignment.data.datasource.remote

import com.adyen.android.assignment.data.datasource.api.PlacesService
import com.adyen.android.assignment.data.datasource.api.VenueRecommendationsQueryBuilder
import com.adyen.android.assignment.data.datasource.api.model.Result
import javax.inject.Inject

class NearbyPlacesRemoteDataSourceImpl @Inject constructor(
    private val placesService: PlacesService
) : NearbyPlacesRemoteDataSource {
    override suspend fun getNearbyPlaces(latitude: Double, longitude: Double): List<Result> {
        return try {
            val query = VenueRecommendationsQueryBuilder()
                .setLatitudeLongitude(latitude, longitude)
                .build()
            val response = placesService.getVenueRecommendations(query)
            response.results ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}
