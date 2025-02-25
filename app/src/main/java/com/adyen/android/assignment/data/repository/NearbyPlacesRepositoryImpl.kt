package com.adyen.android.assignment.data.repository

import com.adyen.android.assignment.data.datasource.remote.NearbyPlacesRemoteDataSource
import com.adyen.android.assignment.data.mapper.NearbyPlacesMapper
import com.adyen.android.assignment.domain.model.NearbyPlace
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NearbyPlacesRepositoryImpl @Inject constructor(
    private val remoteDataSource: NearbyPlacesRemoteDataSource,
    private val nearbyPlacesMapper: NearbyPlacesMapper
) : NearbyPlacesRepository {
    override fun getNearbyPlaces(latitude: Double, longitude: Double): Flow<List<NearbyPlace>> = flow {
        emit(nearbyPlacesMapper.mapList(remoteDataSource.getNearbyPlaces(latitude, longitude)))
    }
}