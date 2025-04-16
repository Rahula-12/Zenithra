package com.assignment.zenithra.screens.home.facedetection

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.RectF
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import java.util.concurrent.Executors

@Composable
fun FaceDetectionScreen(modifier: Modifier = Modifier) {

    var permissionGranted by remember { mutableStateOf(false) }

    RequestCameraPermission {
        permissionGranted = true
    }
    if(permissionGranted) {
        var faceInRectangle by remember { mutableStateOf(false) }
        val referenceRect = remember { Rect(200f, 200f, 1000f, 1000f) }

        Box(modifier = Modifier.fillMaxSize()) {
            CameraPreview(
                modifier = Modifier.fillMaxSize(),
                onFacesDetected = { faces ->
                    faceInRectangle = faces.any { face ->
                        val boundingBox = face.boundingBox
                        val faceRect = RectF(boundingBox)
                        referenceRect.contains(Offset(faceRect.centerX(), faceRect.centerY()))
                    }
                }
            )
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawRect(
                    color = if (faceInRectangle) Color.Green else Color.Red,
                    topLeft = Offset(referenceRect.left, referenceRect.top),
                    size = Size(referenceRect.width, referenceRect.height),
                    style = Stroke(width = 4f)
                )
            }
        }
    }
    else {
        Box(modifier=modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Please provide camera permission", color = Color.White)
        }
    }
}

@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    onFacesDetected: (List<Face>) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    AndroidView(
        factory = { ctx ->
            val previewView = PreviewView(ctx).apply {
                this.scaleType = PreviewView.ScaleType.FILL_CENTER
                layoutParams = android.view.ViewGroup.LayoutParams(
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT
                )
            }

            val cameraExecutor = Executors.newSingleThreadExecutor()
            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            val faceDetector = FaceDetection.getClient(
                FaceDetectorOptions.Builder()
                    .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                    .setContourMode(FaceDetectorOptions.CONTOUR_MODE_NONE)
                    .build()
            )

            imageAnalysis.setAnalyzer(cameraExecutor) { imageProxy ->
                processImageProxy(faceDetector, imageProxy, onFacesDetected)
            }

            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageAnalysis
                    )
                } catch (e: Exception) {
                    Log.e("CameraPreview", "Error starting camera: ${e.message}")
                }
            }, ContextCompat.getMainExecutor(ctx))

            previewView
        },
        modifier = modifier
    )
}

@androidx.annotation.OptIn(ExperimentalGetImage::class)
private fun processImageProxy(
    faceDetector: FaceDetector,
    imageProxy: ImageProxy,
    onFacesDetected: (List<Face>) -> Unit
) {
    val mediaImage = imageProxy.image
    if (mediaImage != null) {
        val inputImage = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
        faceDetector.process(inputImage)
            .addOnSuccessListener { faces ->
                onFacesDetected(faces)
            }
            .addOnFailureListener { e ->
                Log.e("FaceDetection", "Face detection failed: ${e.message}")
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    } else {
        imageProxy.close()
    }
}

@Composable
fun RequestCameraPermission(onPermissionGranted: () -> Unit) {
    val context = LocalContext.current
    val cameraPermissionState = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) onPermissionGranted()
        }
    )

    LaunchedEffect(Unit) {
        when {
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED -> {
                onPermissionGranted()
            }
            else -> {
                cameraPermissionState.launch(Manifest.permission.CAMERA)
            }
        }
    }
}
