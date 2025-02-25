package com.adyen.android.assignment.domain.model

data class NearbyPlaceLocation(
    val address: String?,
    val country: String?,
    val locality: String?,
    val region: String?,
    val postcode: String?
)
