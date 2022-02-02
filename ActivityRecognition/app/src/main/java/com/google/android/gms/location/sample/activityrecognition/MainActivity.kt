/*
 * Copyright 2022 Google, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.gms.location.sample.activityrecognition

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import com.google.android.gms.location.sample.activityrecognition.PlayServicesAvailableState.Initializing
import com.google.android.gms.location.sample.activityrecognition.PlayServicesAvailableState.PlayServicesAvailable
import com.google.android.gms.location.sample.activityrecognition.PlayServicesAvailableState.PlayServicesUnavailable
import com.google.android.gms.location.sample.activityrecognition.ui.ActivityRecognitionPermissionState
import com.google.android.gms.location.sample.activityrecognition.ui.ActivityRecognitionScreen
import com.google.android.gms.location.sample.activityrecognition.ui.InitializingScreen
import com.google.android.gms.location.sample.activityrecognition.ui.ServiceUnavailableScreen
import com.google.android.gms.location.sample.activityrecognition.ui.theme.ActivityRecognitionTheme
import dagger.hilt.android.AndroidEntryPoint

/** The main entry point of the app. */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val permissionState = ActivityRecognitionPermissionState(this) {
            if (it.permissionGranted) {
                Log.d("ActivityRecognition", "TODO: start activity recognition")
            }
        }

        setContent {
            ActivityRecognitionTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(stringResource(id = R.string.app_name))
                            }
                        )
                    }
                ) {
                    MainScreen(viewModel = viewModel, permissionState = permissionState)
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    permissionState: ActivityRecognitionPermissionState
) {
    val uiState by viewModel.playServicesAvailableState.collectAsState()
    when (uiState) {
        Initializing -> InitializingScreen()
        PlayServicesUnavailable -> ServiceUnavailableScreen()
        PlayServicesAvailable -> {
            ActivityRecognitionScreen(
                showDegradedExperience = permissionState.showDegradedExperience,
                needsPermissionRationale = permissionState.needsRationale,
                onButtonClick = { permissionState.requestPermission() }
            )
        }
    }
}
