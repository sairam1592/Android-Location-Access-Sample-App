package com.adyen.android.assignment.data.datasource.api.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Result(
    val categories: List<Category>,
    val distance: Int,
    val geocode: GeoCode?,
    val location: Location,
    val name: String,
    val timezone: String,
)