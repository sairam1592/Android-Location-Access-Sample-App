package com.adyen.android.assignment.data.datasource.api.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Location(
    val address: String?,
    val country: String?,
    val locality: String?,
    val neighbourhood: List<String>?,
    val postcode: String?,
    val region: String?,
)
