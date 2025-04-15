package com.assignment.zenithra.screens.manga

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.RectF
import android.util.Log
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
import com.assignment.zenithra.models.MangaEntity
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import java.util.concurrent.Executors

val mangaSaver = Saver<MangaEntity, Map<String, Any>>(
    save = { mapOf("id" to it.id, "title" to it.title,"thumb" to it.thumb,"summary" to it.summary,"subtitle" to it.subtitle) },
    restore = { MangaEntity(it["id"] as Int,it["title"] as String,it["thumb"] as String,it["summary"] as String,it["subtitle"] as String) }
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MangaScreen(
    onLogOut:()->Unit={},
    modifier: Modifier,
    mangaItems: LazyPagingItems<MangaEntity>
) {
    var mangaSelected by rememberSaveable(stateSaver = mangaSaver) {
        mutableStateOf(MangaEntity(0,"","","",""))
    }
    var showDetail by rememberSaveable { mutableStateOf(false)  }
    var faceRecognition by rememberSaveable { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier=modifier.fillMaxWidth().padding(start = 10.dp, end = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text(
                            if(faceRecognition) "Face Recognition"
                            else if(!showDetail) "Mangas"
                            else mangaSelected.title,overflow= TextOverflow.Ellipsis,modifier=modifier.wrapContentWidth())
                        Text("LOG OUT",modifier=modifier.clickable { onLogOut() })
                    }
                },
                colors = TopAppBarColors(
                    containerColor = Color.Black,
                    scrolledContainerColor = Color.Black,
                    navigationIconContentColor = Color.Black,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.Black
                )
            )
        },
        bottomBar = {
            BottomAppBar {
                Row(modifier=modifier.fillMaxWidth()) {
                    Button(onClick = {
                        faceRecognition=false
                    },modifier=modifier.weight(1f)) {
                        Text("Manga")
                    }
                    Button(onClick = {
                        faceRecognition=true
                    },modifier=modifier.weight(1f)) {
                        Text("Face")
                    }
                }
            }
        }
    ) {innerPadding->
        Box(modifier=modifier.fillMaxSize().padding(innerPadding).background(Color.Black)){
            if(faceRecognition) {
                FaceDetectionScreen()
            }
            else if(!showDetail){
                LazyVerticalGrid(GridCells.Fixed(3)) {
                    items(mangaItems.itemCount) { index ->
                        MangaItem(
                            modifier, mangaItems[index]?.thumb, mangaItems[index]?.title,
                        ){
                            mangaItems[index]?.let {
                                showDetail=true
                                mangaSelected=it
                            }
                        }
                    }
                    item {
                        if (mangaItems.loadState.append is LoadState.Loading) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
            else {
                MangaDetailScreen(modifier,mangaSelected){
                    showDetail=false
                }
            }
        }

    }
}

@Composable
fun MangaItem(
    modifier: Modifier=Modifier,
    thumbUrl:String?="https://usc1.contabostorage.com/scraper/mangas/65a52ea8f64a55128b487e1b/thumb.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=c10e9464b360c31ce8abea9b266076f6%2F20250413%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20250413T181208Z&X-Amz-Expires=86400&X-Amz-SignedHeaders=host&X-Amz-Signature=fdcb3eed4243931db68e7be5022e2e568800bf4be9fc7a8bb569b5ed3bbf3449",title:String?="A World of Gold to You",
    onClick: () -> Unit) {
    AsyncImage(
        model = thumbUrl,
        contentDescription = "$title image",
        modifier =modifier.height(200.dp).width(100.dp).clipToBounds().padding(end=10.dp).clip(
        RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp, topEnd = 20.dp, bottomEnd = 20.dp)
    ).clickable {
            onClick()
        }
    )
}

@Composable
fun MangaDetailScreen(modifier:Modifier=Modifier,mangaEntity: MangaEntity,onBackPressed:()->Unit) {
    BackHandler(true) {
        onBackPressed()
    }
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Row(modifier = modifier.fillMaxWidth()) {
            AsyncImage(
                model=mangaEntity.thumb,
                contentDescription = null,
                modifier=modifier.weight(1f)
            )
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                Text(mangaEntity.title,color=Color.White)
                Text(mangaEntity.subtitle,color=Color.White)
            }
        }
        Text(mangaEntity.summary,color=Color.White)
    }
}


@Composable
fun FaceDetectionScreen(modifier: Modifier=Modifier) {

    var permissionGranted by remember { mutableStateOf(false) }

    RequestCameraPermission {
        permissionGranted = true
    }
    if(permissionGranted) {
        var faceInRectangle by remember { mutableStateOf(false) }

        // Define the reference rectangle (in dp, will be converted to pixels)
        val referenceRect = remember { Rect(200f, 200f, 600f, 600f) }

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

            // Draw the reference rectangle
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
