package com.example.airpic.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.airpic.ui.camera.CameraScreen
import com.example.airpic.ui.no_permission.NoAudioPermissionScreen
import com.example.airpic.ui.no_permission.NoPermissionScreen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainScreen() {

    val cameraPermissionState: PermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
    val audioPermissionState: PermissionState = rememberPermissionState(android.Manifest.permission.RECORD_AUDIO)

    MainContent(
        hasCameraPermission = cameraPermissionState.status.isGranted,
        hasAudioPermission = audioPermissionState.status.isGranted,
        onRequestCameraPermission = cameraPermissionState::launchPermissionRequest,
        onRequestAudioPermission = audioPermissionState::launchPermissionRequest
    )
}

@Composable
private fun MainContent(
    hasCameraPermission: Boolean,
    hasAudioPermission: Boolean,
    onRequestCameraPermission: () -> Unit,
    onRequestAudioPermission: () -> Unit
) {

    if (hasCameraPermission && hasAudioPermission) {
        CameraScreen()
    } else {
        if(!hasCameraPermission){
            NoPermissionScreen(onRequestCameraPermission)
        } else {
            NoAudioPermissionScreen(onRequestAudioPermission)
        }
    }
}

@Preview
@Composable
private fun Preview_MainContent() {
    MainContent(
        hasCameraPermission = true,
        hasAudioPermission = true,
        onRequestCameraPermission = {},
        onRequestAudioPermission = {}
    )
}