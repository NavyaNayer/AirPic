package com.example.airpic.ui.camera



import android.util.Log
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlipCameraAndroid
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    val cameraController = remember { LifecycleCameraController(context) }

    Scaffold(
        modifier = Modifier.fillMaxSize()

    ) { paddingValues: PaddingValues ->
        AndroidView(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            factory = { context ->
                PreviewView(context).apply {
                    layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                    setBackgroundColor(android.graphics.Color.BLACK)
                    scaleType = PreviewView.ScaleType.FILL_START
                }.also { previewView ->
                    previewView.controller = cameraController
                    cameraController.bindToLifecycle(lifecycleOwner)
                }
            })
    }
    val translucentBlack = Color(0x80f5f5f5)

    val galleryButtonModifier = Modifier
        .size(with(LocalDensity.current) { 70.dp })
        .offset(x = 40.dp, y = 75.dp)

    val shutterButtonModifier = Modifier
        .size(with(LocalDensity.current) { 100.dp })
        .offset(x = 155.dp, y = 55.dp)

    val flipCameraButtonModifier = Modifier
        .size(with(LocalDensity.current) { 70.dp })
        .offset(x = (300.dp), y = 75.dp)


    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        Box (
            modifier = Modifier
                .fillMaxWidth()
                .height(215.dp)
                .background(translucentBlack)
        ){FloatingActionButton(
            onClick = {
                Log.d("CameraScreen", "Gallery button clicked")
            },
            modifier = galleryButtonModifier,
            backgroundColor = androidx.compose.ui.graphics.Color(0xFF330066),
            contentColor = androidx.compose.ui.graphics.Color(0XFFFDFEFF)
        ) {
            Icon(
                imageVector = Icons.Default.PhotoLibrary,
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )
        }
        FloatingActionButton(
            onClick = {
                Log.d("CameraScreen", "Shutter button clicked")
            },
            modifier = shutterButtonModifier,
            backgroundColor = androidx.compose.ui.graphics.Color.White,
            contentColor = androidx.compose.ui.graphics.Color.White
        ) {

        }

        FloatingActionButton(
            onClick = {
                Log.d("CameraScreen", "Flip camera button clicked")
            },
            modifier = flipCameraButtonModifier,
            backgroundColor = androidx.compose.ui.graphics.Color(0xFF330066),
            contentColor = androidx.compose.ui.graphics.Color(0XFFFDFEFF)
        ) {
            Icon(
                imageVector = Icons.Default.FlipCameraAndroid,
                contentDescription = null,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}
}

@Preview
@Composable
private fun Preview_CameraContent() {
    CameraContent()
}
