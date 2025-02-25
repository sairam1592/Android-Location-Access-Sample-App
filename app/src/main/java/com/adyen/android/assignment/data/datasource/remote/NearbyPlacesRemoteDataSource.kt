package com.adyen.android.assignment.data.datasource.remote

import com.adyen.android.assignment.data.datasource.api.model.Result

interface NearbyPlacesRemoteDataSource {
    suspend fun getNearbyPlaces(latitude: Double, longitude: Double): List<Result>
}