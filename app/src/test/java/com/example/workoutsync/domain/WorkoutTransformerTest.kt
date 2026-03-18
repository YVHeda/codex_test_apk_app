package com.example.workoutsync.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Instant

class WorkoutTransformerTest {

    private val transformer = WorkoutTransformer()

    @Test
    fun treadmill_is_converted_to_run_for_strava() {
        val workout = SamsungWorkout(
            id = "1",
            type = WorkoutType.TREADMILL,
            startedAt = Instant.parse("2026-03-16T06:00:00Z"),
            endedAt = Instant.parse("2026-03-16T06:30:00Z"),
            title = "Hotel Treadmill",
            groupedSegments = listOf(
                WorkoutSegment(name = "Segment A", durationSeconds = 1800, distanceMeters = 5000.0)
            ),
            biometrics = emptyList()
        )

        val payload = transformer.buildStravaPayload(workout)

        assertEquals("Run", payload.sportType)
        assertEquals(5000.0, payload.distanceMeters, 0.001)
    }

    @Test
    fun grouped_weightlifting_is_single_activity_with_segment_details() {
        val workout = SamsungWorkout(
            id = "2",
            type = WorkoutType.WEIGHTLIFTING,
            startedAt = Instant.parse("2026-03-16T07:00:00Z"),
            endedAt = Instant.parse("2026-03-16T08:00:00Z"),
            title = "Push Pull",
            groupedSegments = listOf(
                WorkoutSegment("Bench Press", durationSeconds = 600, sets = 5, reps = 5),
                WorkoutSegment("Rows", durationSeconds = 600, sets = 4, reps = 10)
            ),
            biometrics = emptyList()
        )

        val payload = transformer.buildStravaPayload(workout)

        assertEquals("WeightTraining", payload.sportType)
        assertTrue(payload.description.contains("Bench Press"))
        assertTrue(payload.description.contains("Rows"))
    }
}
