package com.adyen.android.assignment.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adyen.android.assignment.domain.usecase.GetNearbyPlacesUseCase
import com.adyen.android.assignment.presentation.NearbyPlacesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NearbyPlacesViewModel @Inject constructor(
    private val getNearbyPlacesUseCase: GetNearbyPlacesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<NearbyPlacesState>(NearbyPlacesState.Loading)
    val state: StateFlow<NearbyPlacesState> = _state

    fun fetchNearbyPlaces(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _state.value = NearbyPlacesState.Loading
            runCatching {
                getNearbyPlacesUseCase(latitude, longitude)
                    .collect { venues ->
                        _state.value = NearbyPlacesState.Success(venues)
                    }
            }.onFailure { throwable ->
                _state.value = NearbyPlacesState.Error(throwable.message ?: "Failed to fetch venues")
            }
        }
    }
}