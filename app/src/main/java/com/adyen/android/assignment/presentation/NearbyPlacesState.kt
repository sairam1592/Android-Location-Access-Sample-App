package com.adyen.android.assignment.presentation

import com.adyen.android.assignment.domain.model.NearbyPlace

sealed class NearbyPlacesState {
    object Loading : NearbyPlacesState()
    data class Success(val nearbyPlaces: List<NearbyPlace>) : NearbyPlacesState()
    data class Error(val message: String) : NearbyPlacesState()
}