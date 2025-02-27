package com.adyen.android.assignment.data.location

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import com.adyen.android.assignment.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MyLocationManager @Inject constructor(@ApplicationContext private val context: Context) {

    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    private val locationRequest: LocationRequest = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY,
        LOCATION_UPDATE_INTERVAL
    ).build()

    fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun isLocationEnabled(): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    suspend fun getCurrentLocation(): Location? {
        return try {
            if (hasLocationPermission() && isLocationEnabled()) {
                fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                    .await()
                    ?: fusedLocationClient.lastLocation.await()
            } else {
                null
            }
        } catch (e: SecurityException) {
            null
        }
    }

    private var lastKnownLocation: Location? = null

    fun requestLocationUpdates(): Flow<Location?> = callbackFlow {
        if (!hasLocationPermission() || !isLocationEnabled()) {
            close()
            return@callbackFlow
        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val newLocation = result.lastLocation
                if (newLocation != null && hasSignificantChange(newLocation)) {
                    lastKnownLocation = newLocation
                    trySend(newLocation)
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
        awaitClose { fusedLocationClient.removeLocationUpdates(locationCallback) }
    }

    private fun hasSignificantChange(newLocation: Location): Boolean {
        val last = lastKnownLocation ?: return true
        return newLocation.distanceTo(last) > 100
    }

    fun showEnableLocationDialog(activity: ComponentActivity, onDismiss: () -> Unit) {
        AlertDialog.Builder(activity)
            .setTitle(activity.getString(R.string.alert_dialog_title))
            .setMessage(activity.getString(R.string.alert_dialog_desc))
            .setPositiveButton(activity.getString(R.string.alert_dialog_positive_btn)) { _, _ ->
                activity.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
            .setNegativeButton(activity.getString(R.string.alert_dialog_negative_btn)) { _, _ ->
                onDismiss()
            }
            .show()
    }

    companion object {
        private const val LOCATION_UPDATE_INTERVAL = 10_000L
    }
}
