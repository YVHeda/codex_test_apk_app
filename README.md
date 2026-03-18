# Workout Sync Bridge (Android APK)

This project is an Android app skeleton that syncs workouts from Samsung Health into Strava and Google Fit, with conversion logic for:

1. **Treadmill workouts → Strava `Run` activities**.
2. **Grouped Samsung workouts → one Strava `WeightTraining` activity**, with each sub-workout listed in the description.
3. **Biometric stream attached to each outgoing activity payload** (heart rate/calories/etc) so partners can ingest as supported.

## What is implemented

- A Compose UI with a **Sync Workouts** action.
- A `SyncCoordinator` pipeline:
  - Fetch from Samsung source.
  - Transform into Strava + Google Fit payloads.
  - Upload to both targets.
- Transformation rules in `WorkoutTransformer`.
- Unit tests validating treadmill conversion and grouped strength description behavior.
- Demo repository/gateway classes so you can run end-to-end flow before real SDK/API wiring.

> **Important:** This is a production-oriented foundation. You still need real OAuth + vendor SDK/API implementations for Samsung Health, Strava, and Google Fit.

## Architecture overview

- `SamsungHealthRepository` – source adapter.
- `WorkoutTransformer` – business rules for sport type conversion and description formatting.
- `StravaGateway` + `GoogleFitGateway` – destination adapters.
- `SyncCoordinator` – orchestration.

## Additional APK considerations (before production)

### 1) Samsung Health integration constraints

- Samsung Health data access generally requires Samsung Health Data SDK setup, app registration, and user consent prompts.
- Ensure you request only minimal scopes (activity + biometrics needed for sync).
- Handle devices where Samsung Health is missing, outdated, or permission is revoked.

### 2) Strava API constraints

- Strava needs OAuth2 and approved scopes.
- Some biometric/stream endpoints may require separate upload flow from the base activity create endpoint.
- Respect Strava rate limits and backoff strategy.

### 3) Google Fit status / migration planning

- Google Fit APIs have ongoing migration pressure toward **Health Connect** on Android.
- Consider implementing a `HealthConnectGateway` alongside `GoogleFitGateway` for forward compatibility.

### 4) Privacy & compliance

- Biometric data is sensitive; encrypt at rest and in transit.
- Add explicit consent screens and a “delete my synced data” workflow.
- Publish privacy policy and retention policy before distribution.

### 5) Reliability concerns

- Use `WorkManager` for retryable background sync jobs.
- Add idempotency keys so repeated jobs do not create duplicate Strava activities.
- Persist sync checkpoints (last successful workout timestamp).

### 6) APK signing & release

- Use release keystore + Play App Signing.
- Separate build variants (`debug`, `staging`, `release`) with different OAuth client IDs.
- Keep API keys out of source (Gradle secrets / CI secrets manager).

## Method to test this app

### Local developer test

1. Build and run on Android Studio emulator/device.
2. Tap **Sync Workouts**.
3. Validate log output for both destination uploads.
4. Run unit tests:
   - `./gradlew test`

### Integration test plan (recommended)

1. Replace demo classes with sandbox implementations:
   - Samsung test account with treadmill + grouped strength samples.
   - Strava test app credentials.
   - Google Fit (or Health Connect) test app credentials.
2. Execute these scenarios:
   - **Scenario A:** treadmill workout syncs as Strava `Run` with distance + biometrics.
   - **Scenario B:** grouped strength workout appears as one Strava weight-training activity with sub-entries in description.
   - **Scenario C:** biometric data points are associated with each corresponding synced workout.
3. Negative tests:
   - Expired OAuth token refresh.
   - Permission revoked mid-sync.
   - Duplicate sync retry should not duplicate activities.

## Next implementation tasks

1. Implement real Samsung Health Data SDK repository.
2. Implement Strava OAuth + upload API client.
3. Implement Google Fit OAuth (or Health Connect migration target).
4. Add encrypted local storage for tokens and checkpoints.
5. Add WorkManager background sync and retry policies.
