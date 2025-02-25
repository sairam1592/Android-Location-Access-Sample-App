package com.adyen.android.assignment.domain.usecase

import com.adyen.android.assignment.data.repository.NearbyPlacesRepository
import com.adyen.android.assignment.domain.model.NearbyPlace
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNearbyPlacesUseCase @Inject constructor(
    private val repository: NearbyPlacesRepository
) {
    operator fun invoke(latitude: Double, longitude: Double): Flow<List<NearbyPlace>> {
        return repository.getNearbyPlaces(latitude, longitude)
    }
}