package com.adyen.android.assignment.data.datasource.api.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GeoCode(
    val main: Main
)

@JsonClass(generateAdapter = true)
data class Main(
    val latitude: Double,
    val longitude: Double,
)