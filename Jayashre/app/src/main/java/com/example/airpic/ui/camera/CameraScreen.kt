package com.example.airpic.ui.camera



import android.content.ContentValues
import android.content.ContentValues.TAG
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
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
    val view = LayoutInflater.from(context).inflate(R.layout.activity_main, null)

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
                .fillMaxHeight(),
            factory = { context ->
                view
            })
    }

    var isTorchOn by remember { mutableStateOf(false) }
    val flashButton = view.findViewById<ImageButton>(R.id.flashButton)
    flashButton.setOnClickListener {
        isTorchOn = !isTorchOn

        flashButton.setImageResource(
            if (isTorchOn) R.drawable.ic_flash_on
            else R.drawable.ic_flash_off
        )

        /*imageCapture.flashMode =
            if (isTorchOn) ImageCapture.FLASH_MODE_ON
            else ImageCapture.FLASH_MODE_OFF*/ //not working
    }

    var timerState = 0
    val timerButton = view.findViewById<ImageButton>(R.id.timerButton)
    fun updateTimer() {
        when (timerState) {
            0 -> {
                timerButton.setImageResource(R.drawable.ic_timer)
            }
            1 -> {
                timerButton.setImageResource(R.drawable.ic_three_timer)
            }
            2 -> {
                timerButton.setImageResource(R.drawable.ic_ten_timer)
            }
        }
    }

    updateTimer()
    timerButton.setOnClickListener{
        timerState = (timerState + 1) % 3
        updateTimer()
    }

    var isGestureOn by remember { mutableStateOf(false) }
    val gestureButton = view.findViewById<ImageButton>(R.id.gestureButton)
    gestureButton.setOnClickListener{
        isGestureOn = !isGestureOn

        gestureButton.setImageResource(
            if (isGestureOn) R.drawable.ic_hand_gesture_clicked
            else R.drawable.ic_hand_gesture
        )

    }

    val shutterButton = view.findViewById<ImageButton>(R.id.shutterButton)
    val photoModeButton = view.findViewById<Button>(R.id.photoButton)
    val videoModeButton = view.findViewById<Button>(R.id.videoButton)

    fun setPhotoMode() {
        cameraMode = CameraMode.PHOTO
        photoModeButton.setBackgroundResource(R.drawable.ic_rounded_purple_button)
        photoModeButton.setTextColor(Color.WHITE)
        videoModeButton.setBackgroundResource(R.drawable.ic_rounded_white_button)
        videoModeButton.setTextColor(Color.BLACK)
        shutterButton.setImageResource(R.drawable.ic_shutter)
    }

    fun setVideoMode() {
        cameraMode = CameraMode.VIDEO
        videoModeButton.setBackgroundResource(R.drawable.ic_rounded_purple_button)
        videoModeButton.setTextColor(Color.WHITE)
        photoModeButton.setBackgroundResource(R.drawable.ic_rounded_white_button)
        photoModeButton.setTextColor(Color.BLACK)
        shutterButton.setImageResource(R.drawable.ic_record_button)
    }

    setPhotoMode()

    photoModeButton.setOnClickListener {
        if (cameraMode != CameraMode.PHOTO) {
            setPhotoMode()
        }
    }

    videoModeButton.setOnClickListener {
        if (cameraMode != CameraMode.VIDEO) {
            setVideoMode()
        }
    }


    fun toggleCamerafunc () {
        cameraController.cameraSelector =
            if (cameraController.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                CameraSelector.DEFAULT_FRONT_CAMERA
            } else {
                CameraSelector.DEFAULT_BACK_CAMERA
            }
    }
    val flipcamera = view.findViewById<ImageButton>(R.id.flipCameraButton)
    flipcamera.setOnClickListener {
        toggleCamerafunc()
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

    shutterButton.setOnClickListener{
        takePhoto()
    }

}

@Preview
@Composable
private fun Preview_CameraContent() {
    CameraContent()
}
