package com.example.airpic.ui.camera


import android.graphics.Color
import android.util.Log
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CameraScreen (
    viewModel: CameraViewModel = viewModel()
) {
    CameraContent()
}


@Composable
private fun CameraContent() {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraController = remember { LifecycleCameraController(context)}

    Scaffold (
        modifier = Modifier.fillMaxSize()

    ){ paddingValues: PaddingValues ->
    AndroidView(
        modifier = Modifier.fillMaxSize().padding(paddingValues),
        factory = {context ->
        PreviewView(context).apply {
            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            setBackgroundColor(Color.BLACK)
            scaleType = PreviewView.ScaleType.FILL_START
        }.also {previewView ->
            previewView.controller = cameraController
            cameraController.bindToLifecycle(lifecycleOwner)
        }
    })
    }
    val fabSize = with(LocalDensity.current) {80.dp}
    val fabBackgroundColor = androidx.compose.ui.graphics.Color.White
    val fabPadding = 16.dp

    // Wrap the FAB inside a Box to control its position
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = fabPadding)
            .offset (x= -(165.dp) , y=-(75.dp)),
        contentAlignment = Alignment.BottomEnd
    ) {
        FloatingActionButton(
            onClick = {
                // Handle button click action here
                Log.d("CameraScreen", "Shutter button clicked") // Log message to the terminal
            },
            modifier = Modifier
                .size(fabSize),
            backgroundColor = fabBackgroundColor,
            contentColor = androidx.compose.ui.graphics.Color.White
        ) {
            // No content inside the button (empty)
        }
    }
}

@Preview
@Composable
private fun Preview_CameraContent() {
    CameraContent()
}
