package pt.iade.ei.bestumbrella1.views

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.util.Log
import android.util.Size
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import java.util.concurrent.Executors


@SuppressLint("SuspiciousIndentation")
@androidx.annotation.OptIn(ExperimentalGetImage::class)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalGetImage::class)
@Composable
fun QrScannerScreen(
    navController: NavController = rememberNavController(), onCodeScanned: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    var startScanner by remember { mutableStateOf(false) }
    var cameraProviderRef by remember { mutableStateOf<ProcessCameraProvider?>(null) }
    var cameraRef by remember { mutableStateOf<androidx.camera.core.Camera?>(null) }
    var torchEnabled by remember { mutableStateOf(false) }
    var shouldStartAfterPermission by remember { mutableStateOf(false) }
    var scannedText by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }
    DisposableEffect(Unit) {
        onDispose { cameraExecutor.shutdown() }
    }

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted
        if (granted && shouldStartAfterPermission) {
            startScanner = true
            shouldStartAfterPermission = false
        }
    }




    LaunchedEffect(Unit) {
        startScanner = false
        shouldStartAfterPermission = false

    }

    Scaffold(
        bottomBar = { AppBottomNavigationBar(navController) }) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF90CAF9), Color.White)
                    )
                )
        ) {
            if (!startScanner || !hasCameraPermission) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Scanner QR",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Spacer(Modifier.height(50.dp))
                    Text(
                        "Escaneie o código QR do guarda-chuva para desbloquear",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black
                    )
                    Spacer(Modifier.height(50.dp))
                    Icon(
                        Icons.Default.QrCodeScanner,
                        contentDescription = null,
                        modifier = Modifier.size(96.dp),
                        tint = Color.Black
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        "Pronto para escanear",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Text(
                        "Toque no botão abaixo para ativar a câmera",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Black,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Spacer(Modifier.height(35.dp))
                    Button(
                        onClick = {
                            if (hasCameraPermission) {
                                scannedText = ""
                                startScanner = true
                            } else {
                                shouldStartAfterPermission = true
                                launcher.launch(Manifest.permission.CAMERA)
                            }
                        }, colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1976D2), contentColor = Color.White
                        )
                    ) {
                        Icon(Icons.Default.CameraAlt, contentDescription = null, tint = Color.White)
                        Spacer(Modifier.width(8.dp))
                        Text("Iniciar Scanner", color = Color.White)
                    }
                    Spacer(Modifier.height(12.dp))

                    Spacer(Modifier.height(50.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFBBDEFB))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "Como usar:",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.Black
                            )
                            Spacer(Modifier.height(8.dp))
                            Text("1. Dirija-se a uma estação", color = Color.Black)
                            Text("2. Toque em \"Iniciar Scanner\"", color = Color.Black)
                            Text("3. Aponte a câmera para o código QR", color = Color.Black)
                            Text("4. Aguarde o desbloqueio automático", color = Color.Black)
                        }
                    }
                }
            }

            if (startScanner && hasCameraPermission) {
                Box(modifier = Modifier.fillMaxSize()) {
                    AndroidView(
                        modifier = Modifier.fillMaxSize(), factory = { ctx ->
                            val previewView = PreviewView(ctx)
                            previewView.scaleType = PreviewView.ScaleType.FILL_CENTER
                            previewView.implementationMode =
                                PreviewView.ImplementationMode.COMPATIBLE
                            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

                            cameraProviderFuture.addListener({
                                val provider = cameraProviderFuture.get()
                                cameraProviderRef = provider
                                val preview = androidx.camera.core.Preview.Builder().build().also {
                                    it.setSurfaceProvider(previewView.surfaceProvider)
                                }

                                val imageAnalyzer = ImageAnalysis.Builder()
                                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                                    .setTargetResolution(Size(1280, 720)).build().also {
                                        it.setAnalyzer(cameraExecutor, BarcodeAnalyser { code ->
                                            if (scannedText != code) {
                                                scannedText = code
                                                Toast.makeText(
                                                    ctx, "Código: $code", Toast.LENGTH_SHORT
                                                ).show()
                                                startScanner = false
                                                val resolved = resolveCodeForNav(code)
                                                onCodeScanned(resolved)
                                                cameraProviderRef?.unbindAll()
                                            }
                                        })
                                    }

                                try {
                                    provider.unbindAll()
                                    val cam = provider.bindToLifecycle(
                                        lifecycleOwner,
                                        CameraSelector.DEFAULT_BACK_CAMERA,
                                        preview,
                                        imageAnalyzer
                                    )
                                    cameraRef = cam
                                } catch (e: Exception) {
                                    Log.e("QR", "Erro na câmara: ${e.message}")
                                }
                            }, ContextCompat.getMainExecutor(ctx))

                            previewView
                        })

                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(280.dp)
                                .align(Alignment.Center)
                                .border(
                                    width = 3.dp,
                                    color = Color.White,
                                    shape = RoundedCornerShape(16.dp)
                                )
                        )

                        IconButton(
                            onClick = {
                                torchEnabled = !torchEnabled
                                val hasFlash = cameraRef?.cameraInfo?.hasFlashUnit() == true
                                if (hasFlash) {
                                    cameraRef?.cameraControl?.enableTorch(torchEnabled)
                                } else {
                                    torchEnabled = false
                                    Toast.makeText(
                                        context, "Sem flash disponível", Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }, modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(16.dp)
                        ) {
                            Icon(
                                imageVector = if (torchEnabled) Icons.Default.FlashOn else Icons.Default.FlashOff,
                                contentDescription = "Flash",
                                tint = Color.White
                            )
                        }

                        IconButton(
                            onClick = {
                                startScanner = false
                                cameraProviderRef?.unbindAll()
                            }, modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Fechar",
                                tint = Color.White
                            )
                        }


                    }
                }
            }

        }
    }
}

private fun resolveCodeForNav(input: String): String {
    val s = input.trim()
    val qIdx = s.indexOf('?')
    if (s.startsWith("bumb://")) {
        val query = if (qIdx >= 0) s.substring(qIdx + 1) else ""
        for (part in query.split('&')) {
            val eq = part.indexOf('=')
            val key = if (eq >= 0) part.substring(0, eq) else part
            val valStr = if (eq >= 0) part.substring(eq + 1) else ""
            if (key.equals("code", ignoreCase = true)) {
                return java.net.URLDecoder.decode(valStr, Charsets.UTF_8)
            }
        }
        return ""
    }
    if (s.startsWith("http://") || s.startsWith("https://")) {
        val query = if (qIdx >= 0) s.substring(qIdx + 1) else ""
        for (part in query.split('&')) {
            val eq = part.indexOf('=')
            val key = if (eq >= 0) part.substring(0, eq) else part
            val valStr = if (eq >= 0) part.substring(eq + 1) else ""
            if (key.equals("code", ignoreCase = true)) {
                return java.net.URLDecoder.decode(valStr, Charsets.UTF_8)
            }
        }
    }
    return s
}
