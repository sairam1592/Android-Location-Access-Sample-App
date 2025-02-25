package com.adyen.android.assignment.data.repository

import com.adyen.android.assignment.domain.model.NearbyPlace
import kotlinx.coroutines.flow.Flow

interface NearbyPlacesRepository {
    fun getNearbyPlaces(latitude: Double, longitude: Double): Flow<List<NearbyPlace>>
}