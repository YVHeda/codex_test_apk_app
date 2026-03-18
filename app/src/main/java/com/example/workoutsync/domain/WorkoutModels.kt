package com.example.workoutsync.domain

import java.time.Instant

enum class WorkoutType {
    TREADMILL,
    RUN,
    WEIGHTLIFTING,
    OTHER
}

data class BiometricSample(
    val timestamp: Instant,
    val heartRateBpm: Int,
    val caloriesBurned: Double,
    val vo2Estimate: Double? = null
)

data class WorkoutSegment(
    val name: String,
    val durationSeconds: Long,
    val reps: Int? = null,
    val sets: Int? = null,
    val distanceMeters: Double? = null,
    val notes: String? = null
)

data class SamsungWorkout(
    val id: String,
    val type: WorkoutType,
    val startedAt: Instant,
    val endedAt: Instant,
    val title: String,
    val groupedSegments: List<WorkoutSegment>,
    val biometrics: List<BiometricSample>
)

data class StravaActivityPayload(
    val name: String,
    val sportType: String,
    val startedAt: Instant,
    val durationSeconds: Long,
    val distanceMeters: Double,
    val description: String,
    val biometricStream: List<BiometricSample>
)

data class GoogleFitSessionPayload(
    val name: String,
    val activityType: WorkoutType,
    val startedAt: Instant,
    val endedAt: Instant,
    val metadata: String,
    val biometricStream: List<BiometricSample>
)
