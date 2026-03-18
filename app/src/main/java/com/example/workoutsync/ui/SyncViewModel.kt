package com.example.workoutsync.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutsync.data.DemoSamsungHealthRepository
import com.example.workoutsync.data.LoggingGoogleFitGateway
import com.example.workoutsync.data.LoggingStravaGateway
import com.example.workoutsync.domain.SyncCoordinator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SyncUiState(
    val running: Boolean = false,
    val message: String = "Tap sync to transfer Samsung workouts"
)

class SyncViewModel : ViewModel() {
    private val coordinator = SyncCoordinator(
        samsungHealthRepository = DemoSamsungHealthRepository(),
        stravaGateway = LoggingStravaGateway(),
        googleFitGateway = LoggingGoogleFitGateway()
    )

    private val _uiState = MutableStateFlow(SyncUiState())
    val uiState: StateFlow<SyncUiState> = _uiState.asStateFlow()

    fun syncNow() {
        if (_uiState.value.running) return

        viewModelScope.launch {
            _uiState.value = SyncUiState(running = true, message = "Sync in progress…")
            val report = coordinator.syncAll()
            _uiState.value = SyncUiState(
                running = false,
                message = "Synced ${report.totalWorkouts} workouts • Strava ${report.stravaUploads} • Fit ${report.googleFitUploads}"
            )
        }
    }
}
