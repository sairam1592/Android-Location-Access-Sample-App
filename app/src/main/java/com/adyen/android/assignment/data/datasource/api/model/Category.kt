package com.adyen.android.assignment.data.datasource.api.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Category(
    val icon: Icon,
    val id: String,
    val name: String,
)
