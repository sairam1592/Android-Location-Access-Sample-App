package com.adyen.android.assignment.presentation.view

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.lifecycleScope
import com.adyen.android.assignment.data.location.MyLocationManager
import com.adyen.android.assignment.presentation.view.compose.LocationRequiredScreen
import com.adyen.android.assignment.presentation.view.compose.PlacesScreen
import com.adyen.android.assignment.presentation.viewmodel.NearbyPlacesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var locationManager: MyLocationManager

    private val placesViewModel: NearbyPlacesViewModel by viewModels()

    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            fetchLocationAndPlaces()
        } else {
            showLocationRequiredNotification()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!locationManager.hasLocationPermission()) {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            observeLocationUpdates()
        }

        setupUi()
    }

    override fun onResume() {
        super.onResume()

        if (locationManager.hasLocationPermission() && locationManager.isLocationEnabled()) {
            fetchLocationAndPlaces()
        } else {
            showLocationRequiredNotification()
        }
    }

    private fun observeLocationUpdates() {
        lifecycleScope.launch {
            locationManager.requestLocationUpdates().collectLatest { location ->
                if (location != null) {
                    placesViewModel.fetchNearbyPlaces(location.latitude, location.longitude)
                } else {
                    showLocationRequiredNotification()
                }
            }
        }
    }

    private fun fetchLocationAndPlaces() {
        lifecycleScope.launch {
            when {
                !locationManager.hasLocationPermission() -> {
                    locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }

                !locationManager.isLocationEnabled() -> {
                    locationManager.showEnableLocationDialog(this@MainActivity) {
                        showLocationRequiredNotification()
                    }
                }

                else -> {
                    val location = locationManager.getCurrentLocation()
                    if (location != null) {
                        placesViewModel.fetchNearbyPlaces(location.latitude, location.longitude)
                    } else {
                        showLocationRequiredNotification()
                    }
                }
            }
        }
    }

    private fun showLocationRequiredNotification() {
        setContent {
            LocationRequiredScreen(onEnableLocationClick = {
                openLocationSettings()
            })
        }
    }

    private fun openLocationSettings() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }

    private fun setupUi() {
        lifecycleScope.launch {
            placesViewModel.state.collectLatest { state ->
                setContent {
                    if (!locationManager.hasLocationPermission() || !locationManager.isLocationEnabled()) {
                        LocationRequiredScreen(onEnableLocationClick = { openLocationSettings() })
                    } else {
                        PlacesScreen(
                            state = state,
                            onFetchPlaces = { fetchLocationAndPlaces() }
                        )
                    }
                }
            }
        }
    }
}
