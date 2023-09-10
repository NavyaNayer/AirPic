package com.example.airpic.ui.camera



import android.content.ContentValues
import android.content.ContentValues.TAG
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlipCameraAndroid
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.airpic.R
import java.util.Locale

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
    }

    val galleryButtonModifier = Modifier
        .size(with(LocalDensity.current) { 70.dp })
        .offset(x = 40.dp, y = 75.dp)

    val shutterButtonModifier = Modifier
        .size(with(LocalDensity.current) { 100.dp })
        .offset(x = 155.dp, y = 55.dp)

    val flipCameraButtonModifier = Modifier
        .size(with(LocalDensity.current) { 70.dp })
        .offset(x = (300.dp), y = 75.dp)

    fun toggleCamera () {
        cameraController.cameraSelector = if (cameraController.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
            CameraSelector.DEFAULT_FRONT_CAMERA
        } else {
            CameraSelector.DEFAULT_BACK_CAMERA
        }
    }

    fun takePhoto() {
        val name = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.UK)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/AirPic")
            }
        }
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues)
            .build()
        cameraController.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object: ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}",exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val msg = "Photo captured: ${output.savedUri}"
                    Log.d(TAG,msg)
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) { Button(
        onClick = { cameraMode = CameraMode.PHOTO
            Log.d("CameraScreen", "Photo button clicked") },
        modifier = Modifier
            .background(Color.Transparent)
            .offset(x = -(210.dp), y = -(215.dp))
            .clip(RoundedCornerShape(5.dp)),
        colors = ButtonDefaults.buttonColors(if (cameraMode == CameraMode.PHOTO) Color(0x80330066) else Color(0x80FDFEFF))

    ) {
        Text("Photo",
            color = if (cameraMode == CameraMode.PHOTO) Color(0XFFFDFEFF) else Color.Black,
            fontSize = 15.sp)
    }
        Button(
            onClick = { cameraMode = CameraMode.VIDEO
                Log.d("CameraScreen", "Video button clicked") },
            modifier = Modifier
                .background(Color.Transparent)
                .offset(x = -(115.dp), y = -(215.dp))
                .clip(RoundedCornerShape(5.dp)),
            colors = ButtonDefaults.buttonColors(if (cameraMode == CameraMode.VIDEO) Color(0x80330066) else Color(0x80FDFEFF))

        ) {
            Text("Video",
                color = if (cameraMode == CameraMode.VIDEO) Color(0XFFFDFEFF) else Color.Black, fontSize = 15.sp)
        }

        Box (
            modifier = Modifier
                .fillMaxWidth()
                .height(190.dp)
                .background(Color(0x80f5f5f5))
        ){
            FloatingActionButton(
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
                    takePhoto()
                },
                modifier = shutterButtonModifier,
                backgroundColor = androidx.compose.ui.graphics.Color.Transparent,
                contentColor = androidx.compose.ui.graphics.Color.White

            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_shutter),
                    contentDescription = null,
                    modifier = Modifier.size(100.dp)
                )
            }

            FloatingActionButton(
                onClick = {
                    toggleCamera()
                },
                modifier = flipCameraButtonModifier,
                backgroundColor = androidx.compose.ui.graphics.Color(0xFF330066),
                contentColor = androidx.compose.ui.graphics.Color(0XFFFDFEFF)
            ) {
                Icon(
                    imageVector = Icons.Default.FlipCameraAndroid,
                    contentDescription = null,
                    modifier = Modifier.size(35.dp)
                )
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopEnd
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(Color(0x80f5f5f5))

        ) {
            Button(
                onClick = {
                    Log.d("CameraScreen", "Settings button clicked")
                },
                modifier = Modifier
                    .background(Color.Transparent)
                    .offset(x = -(5.dp), y = 5.dp)
                    .size(with(LocalDensity.current) { 100.dp }),
                colors = ButtonDefaults.buttonColors(Color.Transparent)

            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_settings),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                )
            }

            Button(
                onClick = {
                    Log.d("Camera Screen", "Flash OFF Button Clicked")
                },
                modifier = Modifier
                    .background(Color.Transparent)
                    .offset(x = 101.dp, y = 5.dp)
                    .size(with(LocalDensity.current) { 100.dp }),
                colors = ButtonDefaults.buttonColors(Color.Transparent)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_flash_off_24),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
            }

            Button(
                onClick = {
                    Log.d("Camera Screen", "Timer Button Clicked")
                },
                modifier = Modifier
                    .background(Color.Transparent)
                    .offset(x = 207.dp, y = 5.dp)
                    .size(with(LocalDensity.current) { 100.dp }),
                colors = ButtonDefaults.buttonColors(Color.Transparent)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_timer),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
            }

            Button(
                onClick = {
                    Log.d("Camera Screen", "Gesture Button Clicked")
                },
                modifier = Modifier
                    .background(Color.Transparent)
                    .offset(x = 313.dp, y = 5.dp)
                    .size(with(LocalDensity.current) { 100.dp }),
                colors = ButtonDefaults.buttonColors(Color.Transparent)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_front_hand_24),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
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
