@file:OptIn(ExperimentalMaterial3Api::class)

package com.adyen.android.assignment.presentation.view.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adyen.android.assignment.R
import com.adyen.android.assignment.domain.model.NearbyPlace
import com.adyen.android.assignment.domain.model.NearbyPlaceLocation
import com.adyen.android.assignment.presentation.NearbyPlacesState

@ExperimentalMaterial3Api
@Composable
fun PlacesScreen(
    state: NearbyPlacesState,
    onFetchPlaces: () -> Unit,
    modifier: Modifier = Modifier
) {
    var savedState by remember { mutableStateOf(state) }

    LaunchedEffect(state) {
        savedState = state
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.venue_screen_title)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        when (state) {
            is NearbyPlacesState.Loading -> {
                Box(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is NearbyPlacesState.Success -> {
                LazyColumn(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    itemsIndexed(state.nearbyPlaces, key = { index, venue -> venue.name }) { _, venue ->
                        VenueItem(nearbyPlace = venue)
                        Spacer(modifier = Modifier.height(15.dp))
                    }
                }
            }

            is NearbyPlacesState.Error -> {
                Box(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Error: ${state.message}",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = onFetchPlaces) {
                            Text("Retry")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun VenueItem(
    nearbyPlace: NearbyPlace,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = nearbyPlace.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Categories: ${nearbyPlace.categories.joinToString(", ")}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Distance: ${nearbyPlace.distance}m",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Address: ${nearbyPlace.location.address}, ${nearbyPlace.location.locality}, ${nearbyPlace.location.region}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Timezone: ${nearbyPlace.timezone}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VenueScreenPreview() {
    PlacesScreen(
        state = NearbyPlacesState.Success(
            listOf(
                NearbyPlace(
                    name = "Central Park",
                    distance = 120,
                    categories = listOf("Park", "Outdoor"),
                    location = NearbyPlaceLocation(
                        address = "5th Ave",
                        country = "USA",
                        locality = "New York",
                        region = "NY",
                        postcode = "10022"
                    ),
                    latitude = 40.785091,
                    longitude = -73.968285,
                    timezone = "America/New_York"
                ),
                NearbyPlace(
                    name = "Starbucks",
                    distance = 50,
                    categories = listOf("Cafe", "Coffee"),
                    location = NearbyPlaceLocation(
                        address = "Broadway St",
                        country = "USA",
                        locality = "Los Angeles",
                        region = "CA",
                        postcode = "90015"
                    ),
                    latitude = 34.052235,
                    longitude = -118.243683,
                    timezone = "America/Los_Angeles"
                )
            )
        ),
        onFetchPlaces = {}
    )
}