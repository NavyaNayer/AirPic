package com.example.airpic.ui.camera



import android.view.LayoutInflater
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.camera.core.CameraSelector
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.airpic.R

@Composable
fun CameraScreen (
    viewModel: CameraViewModel = viewModel()
) {
    CameraContent()
}

enum class CameraMode {
    PHOTO,
    VIDEO
}

@Composable
private fun CameraContent() {

    var cameraMode by remember { mutableStateOf(CameraMode.PHOTO) }
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val contentResolver = LocalContext.current.contentResolver
    val cameraController = remember { LifecycleCameraController(context) }
    cameraController.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

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
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight() ,
            factory = { context ->
                val inflater = LayoutInflater.from(context)
                val view = inflater.inflate(R.layout.activity_main, null)
                view

            }
        )

    }


}

@Preview
@Composable
private fun Preview_CameraContent() {
    CameraContent()
}
