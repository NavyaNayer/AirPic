package com.example.airpic.ui.camera



import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Recording
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.video.AudioConfig
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
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

private var recording: Recording? = null
@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun CameraContent() {
    var cameraMode by remember { mutableStateOf(CameraMode.PHOTO) }
    var isTorchOn by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    val contentResolver = LocalContext.current.contentResolver
    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE or
                        CameraController.VIDEO_CAPTURE
            )
        }
    }
    val viewModel = viewModel<CameraViewModel>()
    val bitmaps by viewModel.bitmaps.collectAsState()

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            GalleryBottom(
                bitmaps = bitmaps,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0x80330066))
            )
        }
    ) {padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.BottomEnd
        ) {
            CameraState(
                controller = controller,
                modifier = Modifier
                    .fillMaxSize()
            )
            Button(
                onClick = {
                    cameraMode = CameraMode.PHOTO
                    Log.d("CameraScreen", "Photo button clicked")
                },
                modifier = Modifier
                    .background(Color.Transparent)
                    .offset(x = -(210.dp), y = -(225.dp))
                    .clip(RoundedCornerShape(5.dp)),
                colors = ButtonDefaults.buttonColors(
                    if (cameraMode == CameraMode.PHOTO) Color(
                        0x80330066
                    ) else Color(0x80FDFEFF)
                )

            ) {
                Text(
                    "Photo",
                    color = if (cameraMode == CameraMode.PHOTO) Color(0XFFFDFEFF) else Color.Black
                )
            }
            Button(
                onClick = {
                    cameraMode = CameraMode.VIDEO
                    Log.d("CameraScreen", "Video button clicked")
                },
                modifier = Modifier
                    .background(Color.Transparent)
                    .offset(x = -(115.dp), y = -(225.dp))
                    .clip(RoundedCornerShape(5.dp)),
                colors = ButtonDefaults.buttonColors(
                    if (cameraMode == CameraMode.VIDEO) Color(
                        0x80330066
                    ) else Color(0x80FDFEFF)
                )

            ) {
                Text(
                    "Video",
                    color = if (cameraMode == CameraMode.VIDEO) Color(0XFFFDFEFF) else Color.Black
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color(0x80FFFFFF))
            ) {
                Button(
                    colors = ButtonDefaults.buttonColors(Color(0XFF330066)),
                    onClick = {
                        scope.launch {
                            scaffoldState.bottomSheetState.expand()
                        }

                    },
                    modifier = Modifier
                        .size(with(LocalDensity.current) { 80.dp })
                        .offset(x = 30.dp, y = 60.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.PhotoLibrary,
                        contentDescription = null,
                        modifier = Modifier.size(75.dp),
                        tint = Color(0XFFFFFFFF)
                    )
                }
                Button(
                    border = BorderStroke(4.dp, Color(0XFF330066)),
                    colors = ButtonDefaults.buttonColors(
                        if (cameraMode == CameraMode.VIDEO) Color(
                            0XFFFF0000
                        ) else Color(0XFFFFFFFF)
                    ),
                    onClick = {
                        decideCameramode(
                            context = context,
                            controller = controller,
                            onPhototaken = viewModel::onTakePhoto,
                            cameraMode = cameraMode,
                            contentResolver = contentResolver
                        )
                    },
                    modifier = Modifier
                        .size(with(LocalDensity.current) { 100.dp })
                        .offset(x = 155.dp, y = 47.dp),
                ) {
                }

                Button(
                    colors = ButtonDefaults.buttonColors(Color(0XFF330066)),
                    onClick = {
                        toggleCamera(controller)
                    },
                    modifier = Modifier
                        .size(with(LocalDensity.current) { 80.dp })
                        .offset(x = (300.dp), y = 60.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.Cameraswitch,
                        contentDescription = "Switch camera",
                        modifier = Modifier.size(105.dp),
                        tint = Color(0XFFFFFFFF)
                    )
                }
            }
        }
        Box (
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.TopEnd
        ) {
            Box (
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
                        .offset(x = -(5.dp), y = 1.dp)
                        .size(with(LocalDensity.current) { 100.dp }),
                    colors = ButtonDefaults.buttonColors(Color.Transparent)

                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = Color(0XFF330066),
                    )
                }

                Button (
                    onClick = {
                        isTorchOn = !isTorchOn
                        if (isTorchOn) controller.enableTorch(true)
                        else controller.enableTorch(false)
                    },
                    modifier = Modifier
                        .background(Color.Transparent)
                        .offset(x = 50.dp, y = 1.dp)
                        .size(with(LocalDensity.current) { 100.dp }),
                    colors = ButtonDefaults.buttonColors(Color.Transparent)
                ) {
                    Icon(
                        imageVector = if (isTorchOn) Icons.Default.FlashOn
                        else Icons.Default.FlashOff,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = Color(0XFF330066),
                    )
                }

                Button (
                    onClick = {
                        Log.d("CameraScreen", "Settings button clicked")
                    },
                    modifier = Modifier
                        .background(Color.Transparent)
                        .offset(x = 150.dp, y = 1.dp)
                        .size(with(LocalDensity.current) { 100.dp }),
                    colors = ButtonDefaults.buttonColors(Color.Transparent)
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = Color(0XFF330066),
                    )
                }

                Button (
                    onClick = {
                        Log.d("CameraScreen", "Settings button clicked")
                    },
                    modifier = Modifier
                        .background(Color.Transparent)
                        .offset(x = 200.dp, y = 1.dp)
                        .size(with(LocalDensity.current) { 100.dp }),
                    colors = ButtonDefaults.buttonColors(Color.Transparent)
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = Color(0XFF330066),
                    )
                }
            }
        }
    }

}


private fun decideCameramode(context: Context, controller: LifecycleCameraController, onPhototaken: (Bitmap) -> Unit, cameraMode: CameraMode, contentResolver: ContentResolver) {
    when (cameraMode) {
        CameraMode.PHOTO -> takePhoto(
            context = context,
            controller = controller,
            onPhotoTaken = onPhototaken,
            contentResolver = contentResolver
        )

        CameraMode.VIDEO -> captureVideo(controller, context, contentResolver)
    }
}
private fun toggleCamera (controller: LifecycleCameraController) {
    controller.cameraSelector =
        if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
            CameraSelector.DEFAULT_FRONT_CAMERA
        } else {
            CameraSelector.DEFAULT_BACK_CAMERA
        }
}
@SuppressLint("MissingPermission")
private fun captureVideo(controller: LifecycleCameraController, context: Context, contentResolver: ContentResolver) {

    if (recording != null) {
        recording?.stop()
        recording = null
        return
    }
    val name = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.UK)
        .format(System.currentTimeMillis())
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, name)
        put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/AirPic")
        }
    }

    val mediaStoreOutputOptions = MediaStoreOutputOptions
        .Builder(contentResolver, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        .setContentValues(contentValues)
        .build()


    recording = controller.startRecording(
        mediaStoreOutputOptions,
        AudioConfig.create(true),
        ContextCompat.getMainExecutor(context),
    ) { event ->
        when (event) {
            is VideoRecordEvent.Finalize -> {
                if (event.hasError()) {
                    recording?.close()
                    recording = null

                    Toast.makeText(
                        context,
                        "Video capture failed",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
        Log.d("Video", "Captured Video")
    }
}
private fun takePhoto(
    context: Context,
    controller: LifecycleCameraController,
    onPhotoTaken: (Bitmap) -> Unit,
    contentResolver: ContentResolver
) {/*
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
        .build() */

    controller.takePicture(
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)

                val matrix = Matrix().apply {
                    postRotate(image.imageInfo.rotationDegrees.toFloat())
                }
                val rotatedBitmap = Bitmap.createBitmap(
                    image.toBitmap(),
                    0,
                    0,
                    image.width,
                    image.height,
                    matrix,
                    false
                )

                onPhotoTaken(rotatedBitmap)


            }

            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)
                Log.e("Camera", "Couldn't take photo: ", exception)
            }
        }
    )
}






@Preview
@Composable
private fun Preview_CameraContent() {
    CameraContent()
}
