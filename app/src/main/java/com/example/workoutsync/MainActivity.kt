package com.example.workoutsync

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.workoutsync.ui.SyncViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val vm: SyncViewModel = viewModel()
                    val state by vm.uiState.collectAsStateWithLifecycle()

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Samsung Health → Strava + Google Fit")
                        Text(
                            text = state.message,
                            modifier = Modifier.padding(top = 16.dp, bottom = 24.dp)
                        )

                        Button(onClick = vm::syncNow, enabled = !state.running) {
                            Text("Sync Workouts")
                        }

                        if (state.running) {
                            CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
                        }
                    }
                }
            }
        }
    }
}
