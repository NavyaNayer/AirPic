package com.example.airpic.ui.camera



import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
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
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun CameraContent() {
    var cameraMode by remember { mutableStateOf(CameraMode.PHOTO) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
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
    ) { padding ->
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
                        .size(with(LocalDensity.current) { 90.dp })
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
                    onClick = {
                        decideCameramode(context = context, controller = controller, onPhototaken = viewModel::onTakePhoto, cameraMode = cameraMode)
                    },
                    modifier = Modifier
                        .size(with(LocalDensity.current) { 100.dp })
                        .offset(x = 155.dp, y = 47.dp),
                ) {
                }

                Button(
                    onClick = {
                        toggleCamera(controller)
                    },
                    modifier = Modifier
                        .size(with(LocalDensity.current) { 90.dp })
                        .offset(x = (290.dp), y = 60.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.Cameraswitch,
                        contentDescription = "Switch camera",
                        modifier = Modifier.size(100.dp)
                    )
                }
            }


            /*
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(215.dp)
                            .background(Color.Transparent)
                    ) {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    scaffoldState.bottomSheetState.expand()
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Photo,
                                contentDescription = "Open gallery",
                                modifier = Modifier.size(75.dp)
                            )
                        }

                        IconButton(
                            modifier = Modifier.size(95.dp),
                            onClick = {
                                takePhoto(
                                    context = context,
                                    controller = controller,
                                    onPhotoTaken = viewModel::onTakePhoto
                                )
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_shutter),
                                contentDescription = "Take photo",
                                modifier = Modifier.size(300.dp)
                            )
                        }
                        IconButton(
                            onClick = {
                                controller.cameraSelector =
                                    if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                                        CameraSelector.DEFAULT_FRONT_CAMERA
                                    } else {
                                        CameraSelector.DEFAULT_BACK_CAMERA
                                    }
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Default.Cameraswitch,
                                contentDescription = "Switch camera",
                                modifier = Modifier.size(75.dp)
                            )
                        }
                    }*/
        }
    }

}


private fun decideCameramode(context: Context, controller: LifecycleCameraController, onPhototaken: (Bitmap) -> Unit, cameraMode: CameraMode) {
    when (cameraMode) {
        CameraMode.PHOTO -> takePhoto(
            context = context,
            controller = controller,
            onPhotoTaken = onPhototaken
        )

        CameraMode.VIDEO -> captureVideo()
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
private fun captureVideo() {
    Log.d("Video", "Captured Video")
}
private fun takePhoto(
    context: Context,
    controller: LifecycleCameraController,
    onPhotoTaken: (Bitmap) -> Unit
) {
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
