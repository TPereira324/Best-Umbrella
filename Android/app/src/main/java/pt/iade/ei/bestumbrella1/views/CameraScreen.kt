package pt.iade.ei.bestumbrella1.views

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageCapture.OutputFileOptions
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import android.widget.Toast
import java.io.File
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.Alignment

@Composable
fun CameraPreviewScreen() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var lastPhotoPath by remember { mutableStateOf<String?>(null) }
    val imageCapture = remember { ImageCapture.Builder().build() }

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> hasCameraPermission = granted }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text("Visualização da Câmera", style = MaterialTheme.typography.headlineMedium, color = Color.Black, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))

        if (hasCameraPermission) {
            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp),
                factory = { ctx ->
                    val previewView = PreviewView(ctx)
                    // Melhor compatibilidade e preenchimento da área para evitar tela preta
                    previewView.scaleType = PreviewView.ScaleType.FILL_CENTER
                    previewView.implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()
                        val preview = Preview.Builder().build().also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }

                        try {
                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(
                                lifecycleOwner,
                                CameraSelector.DEFAULT_BACK_CAMERA,
                                preview,
                                imageCapture
                            )
                        } catch (e: Exception) {
                            Log.e("CameraPreview", "Erro ao iniciar câmera: ${e.message}")
                        }
                    }, ContextCompat.getMainExecutor(ctx))

                    previewView
                }
            )

            Spacer(Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Button(onClick = {
                    val photoFile = File(context.cacheDir, "photo_${System.currentTimeMillis()}.jpg")
                    val outputOptions = OutputFileOptions.Builder(photoFile).build()
                    imageCapture.takePicture(
                        outputOptions,
                        ContextCompat.getMainExecutor(context),
                        object : ImageCapture.OnImageSavedCallback {
                            override fun onError(exception: ImageCaptureException) {
                                Toast.makeText(context, "Erro ao tirar foto", Toast.LENGTH_SHORT).show()
                                Log.e("CameraPreview", "Capture error: ${exception.message}")
                            }
                            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                                lastPhotoPath = photoFile.absolutePath
                                Toast.makeText(context, "Foto guardada", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }) {
                    Icon(Icons.Default.CameraAlt, contentDescription = null, tint = Color.Black)
                    Spacer(Modifier.width(8.dp))
                    Text("Tirar Foto", color = Color.Black)
                }
            }

            lastPhotoPath?.let { path ->
                Spacer(Modifier.height(16.dp))
                Text("Última foto:", color = Color.Black, fontWeight = FontWeight.Bold)
                val bitmap = BitmapFactory.decodeFile(path)
                bitmap?.let {
                    Image(bitmap = it.asImageBitmap(), contentDescription = null, modifier = Modifier.height(200.dp).fillMaxWidth())
                }
            }
        } else {
            Text("Permissão de câmera não concedida.", color = Color.Black, fontWeight = FontWeight.Bold)
        }
    }
}


