package com.example.airpic.ui.camera



import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Recording
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.video.AudioConfig
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.FrontHand
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Timer10Select
import androidx.compose.material.icons.filled.Timer3Select
import androidx.compose.material.icons.filled.TimerOff
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.airpic.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale


@Composable
fun CameraScreen (
    navController: NavController? = null
) {
    CameraContent(navController)
}

enum class CameraMode {
    PHOTO,
    VIDEO
}

private var recording: Recording? = null
private var isRecording = mutableStateOf(false)
//private var frameBitmap: Bitmap? = null


@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun CameraContent(navController: NavController?) {

    var cameraMode by remember { mutableStateOf(CameraMode.PHOTO) }
    var isTorchOn by remember { mutableStateOf(false) }
    var timerState by remember { mutableIntStateOf(0) }
    var timerValue by remember { mutableStateOf(0) }
    var isTimerActive by remember { mutableStateOf(false) }
    var isCountdownFinished by remember { mutableStateOf(false) }
    var isShutterButtonClicked by remember { mutableStateOf(false) }
    var isGestureOn by remember { mutableStateOf(false) }
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
    //val model = SmileDetectionModel.newInstance(context)

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
                Box(
                    modifier = Modifier
                        .size(with(LocalDensity.current) { 100.dp })
                        .offset(x = 155.dp, y = 47.dp)
                        .border(4.dp, Color(0xFF330066), shape = CircleShape)
                ) {
                    IconButton(
                        onClick = {

                            if (isTimerActive) {
                                isShutterButtonClicked = true
                            } else {
                                decideCameraMode(
                                    context = context,
                                    controller = controller,
                                    cameraMode = cameraMode,
                                    contentResolver = contentResolver,
                                    isRecording = isRecording
                                )
                            }
                        },
                        modifier = Modifier.size(100.dp)
                    ) {
                        val iconRes = if (cameraMode == CameraMode.VIDEO) {
                            if (isRecording.value) R.drawable.ic_record_stop else R.drawable.ic_shutter
                        } else {
                            R.drawable.ic_shutter
                        }
                        Icon(
                            painterResource(iconRes),
                            contentDescription = null,
                            modifier = Modifier.size(100.dp),
                            tint = if (cameraMode == CameraMode.VIDEO) {
                                if (isRecording.value) {
                                    Color(0xFF5c05b3)
                                } else {
                                    Color.Red
                                }
                            } else {
                                Color.White
                            }
                                )
                    }
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
                        navController?.navigate("infoFragment")
                        Log.d("CameraScreen", "Info Button")
                    },
                    modifier = Modifier
                        .background(Color.Transparent)
                        .offset(x = 10.dp, y = 1.dp)
                        .size(with(LocalDensity.current) { 100.dp }),
                    colors = ButtonDefaults.buttonColors(Color.Transparent)

                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = Color(0XFF330066),
                    )
                }

                Button(
                    onClick = {
                        isTorchOn = !isTorchOn
                        if (isTorchOn) controller.enableTorch(true)
                        else controller.enableTorch(false)
                    },
                    modifier = Modifier
                        .background(Color.Transparent)
                        .offset(x = 105.dp, y = 1.dp)
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

                Button(
                    onClick = {
                        timerState = (timerState + 1) % 3
                        timerValue = when (timerState) {
                            0 -> 0
                            1 -> 3
                            2 -> 10
                            else -> 0
                        }
                        isTimerActive = timerValue > 0
                    },
                    modifier = Modifier
                        .background(Color.Transparent)
                        .offset(x = 200.dp, y = 1.dp)
                        .size(with(LocalDensity.current) { 100.dp }),
                    colors = ButtonDefaults.buttonColors(Color.Transparent)
                ) {

                    val timerIcon = when (timerState) {
                        0 -> Icons.Default.TimerOff
                        1 -> Icons.Default.Timer3Select
                        2 -> Icons.Default.Timer10Select
                        else -> Icons.Default.TimerOff
                    }
                    Icon(
                        imageVector = timerIcon,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = Color(0XFF330066),
                    )
                }

                Button(
                    onClick = {
                        isGestureOn = !isGestureOn
                    },
                    modifier = Modifier
                        .background(Color.Transparent)
                        .offset(x = 295.dp, y = 1.dp)
                        .size(with(LocalDensity.current) { 100.dp }),
                    colors = ButtonDefaults.buttonColors(Color.Transparent)
                ) {
                    Icon(
                        imageVector = Icons.Default.FrontHand,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = if (isGestureOn) Color(0XFFFFFF00)
                        else Color(0XFF330066),
                    )
                }
            }
        }

        if (isTimerActive && isShutterButtonClicked) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xAA000000)),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = timerValue.toString(),
                    color = Color.White,
                    style = LocalTextStyle.current.copy(fontSize = 50.sp)
                )
                LaunchedEffect(Unit) {

                    for (count in timerValue downTo 2) {
                        delay(1000)

                        timerValue = count - 1
                    }


                    isCountdownFinished = true
            }}
        }

        LaunchedEffect(isCountdownFinished) {
            if (isCountdownFinished) {

                decideCameraMode(
                    context = context,
                    controller = controller,
                    cameraMode = cameraMode,
                    contentResolver = contentResolver,
                    isRecording = isRecording
                )
                isShutterButtonClicked = false
                isCountdownFinished = false
            }

    }}




}


private fun decideCameraMode(context: Context, controller: LifecycleCameraController, cameraMode: CameraMode, contentResolver: ContentResolver, isRecording: MutableState<Boolean>) {
    when (cameraMode) {
        CameraMode.PHOTO -> takePhoto(
            context = context,
            controller = controller,
            contentResolver = contentResolver
        )

        CameraMode.VIDEO -> captureVideo(controller, context, contentResolver, isRecording)
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
private fun captureVideo(controller: LifecycleCameraController, context: Context, contentResolver: ContentResolver, isRecording: MutableState<Boolean>) {

    if (recording != null) {
        recording?.stop()
        recording = null
        isRecording.value = false
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
                isRecording.value = false
            }
        }
        Log.d("Video", "Captured Video")
    }
    isRecording.value = true
}
private fun takePhoto(
    context: Context,
    controller: LifecycleCameraController,
    contentResolver: ContentResolver,
) {
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

    controller.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
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




/*
val model = SmileDetectionModel.newInstance(context)

// Creates inputs for reference.
val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 100, 100, 3), DataType.FLOAT32)
inputFeature0.loadBuffer(byteBuffer)

// Runs model inference and gets result.
val outputs = model.process(inputFeature0)
val outputFeature0 = outputs.outputFeature0AsTensorBuffer

// Releases model resources if no longer used.
model.close()
*
*/






@Preview
@Composable
private fun Preview_CameraContent() {
    CameraContent(navController = null)
}
