package com.example.workoutsync.data

import com.example.workoutsync.domain.GoogleFitSessionPayload
import com.example.workoutsync.domain.SamsungWorkout
import com.example.workoutsync.domain.StravaActivityPayload
import com.example.workoutsync.domain.WorkoutType
import java.time.Instant

interface SamsungHealthRepository {
    suspend fun fetchRecentWorkouts(): List<SamsungWorkout>
}

interface StravaGateway {
    suspend fun uploadActivity(payload: StravaActivityPayload): Result<Unit>
}

interface GoogleFitGateway {
    suspend fun uploadSession(payload: GoogleFitSessionPayload): Result<Unit>
}

/**
 * Demo implementation that should be replaced by Samsung Health Data SDK integration.
 */
class DemoSamsungHealthRepository : SamsungHealthRepository {
    override suspend fun fetchRecentWorkouts(): List<SamsungWorkout> {
        val start = Instant.parse("2026-03-15T08:00:00Z")
        return listOf(
            SamsungWorkout(
                id = "tm-001",
                type = WorkoutType.TREADMILL,
                startedAt = start,
                endedAt = start.plusSeconds(2400),
                title = "Morning Treadmill",
                groupedSegments = listOf(
                    com.example.workoutsync.domain.WorkoutSegment(
                        name = "Treadmill",
                        durationSeconds = 2400,
                        distanceMeters = 5100.0,
                        notes = "Auto-converted to run on Strava"
                    )
                ),
                biometrics = listOf(
                    com.example.workoutsync.domain.BiometricSample(start.plusSeconds(300), 135, 64.0),
                    com.example.workoutsync.domain.BiometricSample(start.plusSeconds(900), 148, 143.0),
                    com.example.workoutsync.domain.BiometricSample(start.plusSeconds(1800), 156, 271.0)
                )
            ),
            SamsungWorkout(
                id = "wt-001",
                type = WorkoutType.WEIGHTLIFTING,
                startedAt = start.plusSeconds(3600),
                endedAt = start.plusSeconds(6000),
                title = "Upper Body Circuit",
                groupedSegments = listOf(
                    com.example.workoutsync.domain.WorkoutSegment("Bench Press", 600, sets = 5, reps = 5),
                    com.example.workoutsync.domain.WorkoutSegment("Pull-Ups", 500, sets = 4, reps = 8),
                    com.example.workoutsync.domain.WorkoutSegment("Shoulder Press", 550, sets = 4, reps = 10)
                ),
                biometrics = listOf(
                    com.example.workoutsync.domain.BiometricSample(start.plusSeconds(4000), 112, 58.0),
                    com.example.workoutsync.domain.BiometricSample(start.plusSeconds(4800), 126, 91.0),
                    com.example.workoutsync.domain.BiometricSample(start.plusSeconds(5600), 121, 128.0)
                )
            )
        )
    }
}

class LoggingStravaGateway : StravaGateway {
    override suspend fun uploadActivity(payload: StravaActivityPayload): Result<Unit> {
        println("Uploading to Strava: $payload")
        return Result.success(Unit)
    }
}

class LoggingGoogleFitGateway : GoogleFitGateway {
    override suspend fun uploadSession(payload: GoogleFitSessionPayload): Result<Unit> {
        println("Uploading to Google Fit: $payload")
        return Result.success(Unit)
    }
}
