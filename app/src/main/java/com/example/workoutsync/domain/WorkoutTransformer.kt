package com.example.workoutsync.domain

class WorkoutTransformer {

    fun buildStravaPayload(workout: SamsungWorkout): StravaActivityPayload {
        val totalSeconds = workout.endedAt.epochSecond - workout.startedAt.epochSecond
        val description = when (workout.type) {
            WorkoutType.WEIGHTLIFTING -> buildGroupedStrengthDescription(workout.groupedSegments)
            else -> workout.groupedSegments.joinToString(separator = "\n") { segment ->
                "• ${segment.name} (${segment.durationSeconds}s)"
            }
        }

        val stravaSportType = when (workout.type) {
            WorkoutType.TREADMILL -> "Run"
            WorkoutType.RUN -> "Run"
            WorkoutType.WEIGHTLIFTING -> "WeightTraining"
            WorkoutType.OTHER -> "Workout"
        }

        val distance = if (workout.type == WorkoutType.TREADMILL) {
            workout.groupedSegments.sumOf { it.distanceMeters ?: 0.0 }
        } else {
            workout.groupedSegments.sumOf { it.distanceMeters ?: 0.0 }
        }

        return StravaActivityPayload(
            name = workout.title,
            sportType = stravaSportType,
            startedAt = workout.startedAt,
            durationSeconds = totalSeconds,
            distanceMeters = distance,
            description = description,
            biometricStream = workout.biometrics
        )
    }

    fun buildGoogleFitPayload(workout: SamsungWorkout): GoogleFitSessionPayload {
        val description = workout.groupedSegments.joinToString(separator = " | ") {
            listOfNotNull(
                it.name,
                it.sets?.let { sets -> "${sets} sets" },
                it.reps?.let { reps -> "${reps} reps" }
            ).joinToString(" - ")
        }
        return GoogleFitSessionPayload(
            name = workout.title,
            activityType = workout.type,
            startedAt = workout.startedAt,
            endedAt = workout.endedAt,
            metadata = description,
            biometricStream = workout.biometrics
        )
    }

    private fun buildGroupedStrengthDescription(segments: List<WorkoutSegment>): String {
        return buildString {
            appendLine("Imported from Samsung Health grouped workout")
            segments.forEachIndexed { index, segment ->
                val details = listOfNotNull(
                    "${index + 1}. ${segment.name}",
                    segment.sets?.let { "sets=$it" },
                    segment.reps?.let { "reps=$it" },
                    segment.notes
                ).joinToString(" | ")
                appendLine(details)
            }
        }.trim()
    }
}
