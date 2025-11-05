package pt.iade.ei.bestumbrella1.views

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
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
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import java.util.concurrent.Executors
import android.util.Size
import androidx.activity.result.PickVisualMediaRequest
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

@androidx.annotation.OptIn(ExperimentalGetImage::class)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalGetImage::class)
@Composable
fun QrScannerScreen(
    navController: NavController = rememberNavController(),
    onCodeScanned: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    var startScanner by remember { mutableStateOf(false) }
    var cameraProviderRef by remember { mutableStateOf<ProcessCameraProvider?>(null) }
    var cameraRef by remember { mutableStateOf<androidx.camera.core.Camera?>(null) }
    var torchEnabled by remember { mutableStateOf(false) }
    var shouldStartAfterPermission by remember { mutableStateOf(false) }
    var scannedText by remember { mutableStateOf("") }
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

    val scannerOptions = remember {
        BarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_QR_CODE).build()
    }
    val galleryScanner = remember { BarcodeScanning.getClient(scannerOptions) }

    val pickImageLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            try {
                val image = InputImage.fromFilePath(context, uri)
                galleryScanner.process(image)
                    .addOnSuccessListener { barcodes ->
                        val value = barcodes.firstOrNull()?.rawValue
                        if (!value.isNullOrEmpty()) {
                            startScanner = false
                            onCodeScanned(value)
                            Toast.makeText(context, "Código: $value", Toast.LENGTH_SHORT).show()
                            cameraProviderRef?.unbindAll()
                        } else {
                            Toast.makeText(context, "Nenhum QR na imagem", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e("QR", "Erro ao processar imagem: ${e.message}", e)
                        Toast.makeText(context, "Erro ao processar imagem", Toast.LENGTH_SHORT).show()
                    }
            } catch (e: Exception) {
                Log.e("QR", "Falha ao abrir imagem: ${e.message}", e)
                Toast.makeText(context, "Falha ao abrir imagem", Toast.LENGTH_SHORT).show()
            }
        }
    }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            // Solicitar permissão e iniciar automaticamente ao conceder
            shouldStartAfterPermission = true
            launcher.launch(Manifest.permission.CAMERA)
        } else {
            // Se já tiver permissão, iniciar imediatamente
            startScanner = true
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("map") },
                    icon = { Icon(Icons.Default.Map, contentDescription = null) },
                    label = { Text("Mapa") }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = { Icon(Icons.Default.QrCodeScanner, contentDescription = null) },
                    label = { Text("Scanner") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("weather") },
                    icon = { Icon(Icons.Default.Cloud, contentDescription = null) },
                    label = { Text("Tempo") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("history") },
                    icon = { Icon(Icons.Default.History, contentDescription = null) },
                    label = { Text("Histórico") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("profile") },
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    label = { Text("Perfil") }
                )
            }
        }
    ) { padding ->
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
                OutlinedTextField(
                    value = scannedText,
                    onValueChange = { scannedText = it },
                    label = { Text("Resultado do scan") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = {
                            if (scannedText.isNotBlank()) {
                                navController.navigate("rentalDetails/$scannedText")
                            }
                        },
                        enabled = scannedText.isNotBlank()
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = Color.Black)
                        Spacer(Modifier.width(8.dp))
                        Text("Usar código", color = Color.Black)
                    }
                    OutlinedButton(
                        onClick = { scannedText = "" }
                    ) {
                        Icon(Icons.Default.Clear, contentDescription = null, tint = Color.Black)
                        Spacer(Modifier.width(8.dp))
                        Text("Limpar", color = Color.Black)
                    }
                }
                Spacer(Modifier.height(16.dp))
                Text("Scanner QR", style = MaterialTheme.typography.headlineMedium, color = Color.Black)
                Spacer(Modifier.height(50.dp))
                Text(
                    "Escaneie o código QR do guarda-chuva para desbloquear",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
                Spacer(Modifier.height(50.dp))
                Icon(Icons.Default.QrCodeScanner, contentDescription = null, modifier = Modifier.size(96.dp), tint = Color.Black)
                Spacer(Modifier.height(16.dp))
                Text("Pronto para escanear", style = MaterialTheme.typography.titleMedium, color = Color.Black)
                Text("Toque no botão abaixo para ativar a câmera", style = MaterialTheme.typography.bodySmall, color = Color.Black)
                Spacer(Modifier.height(35.dp))
                Button(onClick = {
                    if (hasCameraPermission) {
                        scannedText = ""
                        startScanner = true
                    } else {
                        shouldStartAfterPermission = true
                        launcher.launch(Manifest.permission.CAMERA)
                    }
                }) {
                    Icon(Icons.Default.CameraAlt, contentDescription = null, tint = Color.Black)
                    Spacer(Modifier.width(8.dp))
                    Text("Iniciar Scanner", color = Color.Black)
                }
                Spacer(Modifier.height(12.dp))
                OutlinedButton(onClick = {
                    pickImageLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }) {
                    Icon(Icons.Default.Image, contentDescription = null, tint = Color.Black)
                    Spacer(Modifier.width(8.dp))
                    Text("Ler da galeria", color = Color.Black)
                }
                Spacer(Modifier.height(50.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFBBDEFB))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Como usar:", style = MaterialTheme.typography.titleMedium, color = Color.Black)
                        Spacer(Modifier.height(8.dp))
                        Text("1. Dirija-se a uma estação Best Umbrella", color = Color.Black)
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
                        modifier = Modifier.fillMaxSize(),
                        factory = { ctx ->
                    val previewView = PreviewView(ctx)
                    // Melhor compatibilidade e preenchimento da área
                    previewView.scaleType = PreviewView.ScaleType.FILL_CENTER
                    previewView.implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

                            cameraProviderFuture.addListener({
                                val provider = cameraProviderFuture.get()
                                cameraProviderRef = provider
                                val preview = androidx.camera.core.Preview.Builder().build().also {
                                    it.setSurfaceProvider(previewView.surfaceProvider)
                                }

                                val imageAnalyzer = ImageAnalysis.Builder()
                                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                                    .setTargetResolution(Size(1280, 720))
                                    .build().also {
                                        it.setAnalyzer(cameraExecutor, BarcodeAnalyser { code ->
                                            // Pausar após primeira leitura e mostrar código
                                            if (scannedText != code) {
                                                scannedText = code
                                                Toast.makeText(ctx, "Código: $code", Toast.LENGTH_SHORT).show()
                                                startScanner = false
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
                        }
                    )

                    // Overlay de mira
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
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

                        // Botão de flash
                        IconButton(
                            onClick = {
                                torchEnabled = !torchEnabled
                                val hasFlash = cameraRef?.cameraInfo?.hasFlashUnit() == true
                                if (hasFlash) {
                                    cameraRef?.cameraControl?.enableTorch(torchEnabled)
                                } else {
                                    torchEnabled = false
                                    Toast.makeText(context, "Sem flash disponível", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(16.dp)
                        ) {
                            Icon(
                                imageVector = if (torchEnabled) Icons.Default.FlashOn else Icons.Default.FlashOff,
                                contentDescription = "Flash",
                                tint = Color.White
                            )
                        }

                        // Removidos controles de overlay para usar a linha inferior antiga
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewQrScannerScreen() {
    QrScannerScreen()
}
