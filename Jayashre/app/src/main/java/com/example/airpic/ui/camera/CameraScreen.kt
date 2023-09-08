package com.example.airpic.ui.camera



import android.util.Log
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.camera.core.CameraSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
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
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.common.util.concurrent.ListenableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

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
    val cameraProviderFuture: ListenableFuture<ProcessCameraProvider> = remember { ProcessCameraProvider.getInstance(context) }
    val executor = remember { Executors.newSingleThreadExecutor() }

    CameraPreview(
        cameraProviderFuture = cameraProviderFuture,
        lifecycleOwner = lifecycleOwner,
        executor = executor
    )
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
                Log.d("CameraScreen", "Flip button clicked")
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

@Composable
fun CameraPreview(cameraProviderFuture: ListenableFuture<ProcessCameraProvider>, lifecycleOwner: LifecycleOwner, executor: ExecutorService?)
{
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            val previewView = PreviewView(context)
            previewView.layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            previewView.setBackgroundColor(android.graphics.Color.BLACK)
            previewView.scaleType = PreviewView.ScaleType.FILL_START
            previewView
        },
        update = { view ->
            val cameraProvider = cameraProviderFuture.get()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            val preview = androidx.camera.core.Preview.Builder().build()
            preview.setSurfaceProvider(view.surfaceProvider)

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview)
            } catch (exception: Exception) {
                // Handle exceptions here
            }
        }
    )
}


@Preview
@Composable
private fun Preview_CameraContent() {
    CameraContent()
}
