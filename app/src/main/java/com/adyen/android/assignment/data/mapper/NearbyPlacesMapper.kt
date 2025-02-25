package com.adyen.android.assignment.data.mapper

import com.adyen.android.assignment.data.datasource.api.model.Result
import com.adyen.android.assignment.domain.model.NearbyPlace
import com.adyen.android.assignment.domain.model.NearbyPlaceLocation
import javax.inject.Inject

interface Mapper<I, O> {
    fun map(input: I): O
}

/**
 * Mapper that maps a [Result] to a [NearbyPlace]
 */

class NearbyPlacesMapper @Inject constructor() : Mapper<Result, NearbyPlace> {
    override fun map(input: Result): NearbyPlace {
        return NearbyPlace(
            name = input.name,
            distance = input.distance,
            categories = input.categories.map { it.name },
            location = NearbyPlaceLocation(
                address = input.location.address,
                country = input.location.country,
                locality = input.location.locality,
                region = input.location.region,
                postcode = input.location.postcode
            ),
            latitude = input.geocode?.main?.latitude,
            longitude = input.geocode?.main?.longitude,
            timezone = input.timezone
        )
    }

    fun mapList(results: List<Result>): List<NearbyPlace> = results.map { map(it) }
}