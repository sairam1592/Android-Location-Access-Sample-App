package com.adyen.android.assignment.di

import android.content.Context
import com.adyen.android.assignment.BuildConfig
import com.adyen.android.assignment.data.datasource.api.PlacesService
import com.adyen.android.assignment.data.datasource.remote.NearbyPlacesRemoteDataSource
import com.adyen.android.assignment.data.datasource.remote.NearbyPlacesRemoteDataSourceImpl
import com.adyen.android.assignment.data.location.MyLocationManager
import com.adyen.android.assignment.data.mapper.NearbyPlacesMapper
import com.adyen.android.assignment.data.repository.NearbyPlacesRepository
import com.adyen.android.assignment.data.repository.NearbyPlacesRepositoryImpl
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.FOURSQUARE_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun provideLocationManager(@ApplicationContext context: Context): MyLocationManager {
        return MyLocationManager(context)
    }

    @Provides
    @Singleton
    fun providePlacesService(retrofit: Retrofit): PlacesService {
        return retrofit.create(PlacesService::class.java)
    }

    @Provides
    @Singleton
    fun provideNearbyPlacesRemoteDataSource(service: PlacesService): NearbyPlacesRemoteDataSource {
        return NearbyPlacesRemoteDataSourceImpl(service)
    }

    @Provides
    @Singleton
    fun provideNearbyPlacesRepository(
        remoteDataSource: NearbyPlacesRemoteDataSource,
        nearbyPlacesMapper: NearbyPlacesMapper
    ): NearbyPlacesRepository {
        return NearbyPlacesRepositoryImpl(remoteDataSource, nearbyPlacesMapper)
    }

    @Provides
    @Singleton
    fun provideNearbyPlacesMapper(): NearbyPlacesMapper = NearbyPlacesMapper()
}
