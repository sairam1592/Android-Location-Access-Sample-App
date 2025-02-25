package com.adyen.android.assignment.domain.model

data class NearbyPlace(
    val name: String,
    val distance: Int,
    val categories: List<String>,
    val location: NearbyPlaceLocation,
    val latitude: Double?,
    val longitude: Double?,
    val timezone: String
)
