package pt.iade.ei.bestumbrella1.viewmodels

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

class AlbumViewModel: ViewModel() {
    private val _albumViewState = MutableStateFlow(AlbumViewState())
    val viewStateFlow: StateFlow<AlbumViewState> = _albumViewState.asStateFlow()

    fun onReceive(intent: Intent) = viewModelScope.launch {
        when(intent) {
            is Intent.OnPermissionGrantedWith -> {

                val tempFile = File.createTempFile(
                    "temp_image_file_",
                    ".jpg",
                    intent.compositionContext.cacheDir
                )

                
                val uri = FileProvider.getUriForFile(intent.compositionContext,
                    "pt.iade.ei.bestumbrella1.provider", 
                    tempFile
                )
                _albumViewState.value = _albumViewState.value.copy(tempFileUrl = uri)
            }

            is Intent.OnPermissionDenied -> {
                println("User did not grant permission to use the camera")
            }

            is Intent.OnFinishPickingImagesWith -> {
                if (intent.imageUrls.isNotEmpty()) {
                    val newImages = mutableListOf<ImageBitmap>()
                    for (eachImageUrl in intent.imageUrls) {
                        val inputStream = intent.compositionContext.contentResolver.openInputStream(eachImageUrl)
                        val bytes = inputStream?.readBytes()
                        inputStream?.close()

                        if (bytes != null) {
                            val bitmapOptions = BitmapFactory.Options()
                            bitmapOptions.inMutable = true
                            val bitmap: Bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size, bitmapOptions)
                            newImages.add(bitmap.asImageBitmap())
                        } else {
                            println("The image that was picked could not be read from the device at this url: $eachImageUrl")
                        }
                    }

                    val currentViewState = _albumViewState.value
                    val newCopy = currentViewState.copy(
                        selectedPictures = (currentViewState.selectedPictures + newImages),
                        tempFileUrl = null
                    )
                    _albumViewState.value = newCopy
                } else {
                }
            }

            is Intent.OnImageSavedWith -> {
                val tempImageUrl = _albumViewState.value.tempFileUrl
                if (tempImageUrl != null) {
                    val source = ImageDecoder.createSource(intent.compositionContext.contentResolver, tempImageUrl)

                    val currentPictures = _albumViewState.value.selectedPictures.toMutableList()
                    currentPictures.add(ImageDecoder.decodeBitmap(source).asImageBitmap())

                    _albumViewState.value = _albumViewState.value.copy(tempFileUrl = null,
                        selectedPictures = currentPictures)
                }
            }

            is Intent.OnImageSavingCanceled -> {
                _albumViewState.value = _albumViewState.value.copy(tempFileUrl = null)
            }
        }
    }
}