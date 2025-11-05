package pt.iade.ei.bestumbrella1.views

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import pt.iade.ei.bestumbrella1.viewmodels.AlbumViewState
import pt.iade.ei.bestumbrella1.viewmodels.AlbumViewModel
import pt.iade.ei.bestumbrella1.viewmodels.Intent

@Composable
fun AlbumScreen(
    modifier: Modifier = Modifier, 
    viewModel: AlbumViewModel,
    onBackClick: () -> Unit = {}
) {

    val viewState: AlbumViewState by viewModel.viewStateFlow.collectAsState()
    
    val currentContext = LocalContext.current


    val pickImageFromAlbumLauncher = rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) { urls ->
        viewModel.onReceive(Intent.OnFinishPickingImagesWith(currentContext, urls))
    }


    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { isImageSaved ->
        if (isImageSaved) {
            viewModel.onReceive(Intent.OnImageSavedWith(currentContext))
        } else {

            viewModel.onReceive(Intent.OnImageSavingCanceled)
        }
    }


    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { permissionGranted ->
        if (permissionGranted) {
            viewModel.onReceive(Intent.OnPermissionGrantedWith(currentContext))
        } else {

            viewModel.onReceive(Intent.OnPermissionDenied)
        }
    }


    LaunchedEffect(key1 = viewState.tempFileUrl) {
        viewState.tempFileUrl?.let {
            cameraLauncher.launch(it)
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
            .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Álbum",
            style = androidx.compose.material3.MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Row {
            Button(onClick = {

                permissionLauncher.launch(Manifest.permission.CAMERA)
            }) {
                Text(text = "Take a photo")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = {
                val mediaRequest = PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                pickImageFromAlbumLauncher.launch(mediaRequest)
            }) {
                Text(text = "Pick a picture")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Selected Pictures")
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(0.dp, 1200.dp),
            columns = GridCells.Adaptive(150.dp),
            userScrollEnabled = false
        ) {
            itemsIndexed(viewState.selectedPictures) { index, picture ->
                Image(
                    modifier = Modifier.padding(8.dp),
                    bitmap = picture,
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth
                )
            }
        }
    }
}