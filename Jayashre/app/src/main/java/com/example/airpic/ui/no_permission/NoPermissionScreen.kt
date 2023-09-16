package com.example.airpic.ui.no_permission

import android.view.LayoutInflater
import android.widget.Button
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.example.airpic.R


@Composable
fun NoPermissionScreen(
    onRequestPermission: () -> Unit
) {

    NoPermissionContent(
        onRequestPermission = onRequestPermission
    )
}

@Composable
fun NoPermissionContent(
    onRequestPermission: () -> Unit
) {
    val context = LocalContext.current
    val view = LayoutInflater.from(context).inflate(R.layout.no_camera_permission_screen, null)
    AndroidView(
        factory = { context ->
            view
        },
        modifier = Modifier.fillMaxSize()
    )
    val allowButton = view.findViewById<Button>(R.id.allowCameraButton)
    allowButton.setOnClickListener{
        onRequestPermission()
    }
}

@Preview
@Composable
private fun Preview_NoPermissionContent() {
    NoPermissionContent(
        onRequestPermission = {}
    )
}

@Preview
@Composable
fun NoPermissionContentPreview() {
    NoPermissionContent(onRequestPermission = {})
}
