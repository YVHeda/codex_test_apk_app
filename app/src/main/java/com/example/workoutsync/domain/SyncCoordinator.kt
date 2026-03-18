package com.example.workoutsync.domain

import com.example.workoutsync.data.GoogleFitGateway
import com.example.workoutsync.data.SamsungHealthRepository
import com.example.workoutsync.data.StravaGateway

class SyncCoordinator(
    private val samsungHealthRepository: SamsungHealthRepository,
    private val stravaGateway: StravaGateway,
    private val googleFitGateway: GoogleFitGateway,
    private val transformer: WorkoutTransformer = WorkoutTransformer()
) {
    suspend fun syncAll(): SyncReport {
        val workouts = samsungHealthRepository.fetchRecentWorkouts()
        var stravaSuccess = 0
        var fitSuccess = 0

        workouts.forEach { workout ->
            val stravaPayload = transformer.buildStravaPayload(workout)
            if (stravaGateway.uploadActivity(stravaPayload).isSuccess) {
                stravaSuccess += 1
            }

            val fitPayload = transformer.buildGoogleFitPayload(workout)
            if (googleFitGateway.uploadSession(fitPayload).isSuccess) {
                fitSuccess += 1
            }
        }

        return SyncReport(
            totalWorkouts = workouts.size,
            stravaUploads = stravaSuccess,
            googleFitUploads = fitSuccess
        )
    }
}

data class SyncReport(
    val totalWorkouts: Int,
    val stravaUploads: Int,
    val googleFitUploads: Int
)
