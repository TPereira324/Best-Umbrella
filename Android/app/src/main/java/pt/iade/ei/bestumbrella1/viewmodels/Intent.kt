package pt.iade.ei.bestumbrella1.viewmodels

import android.content.Context
import android.net.Uri

/**
 * User generated events that can be triggered from the UI.
 */
sealed class Intent {
    data class OnPermissionGrantedWith(val compositionContext: Context) : Intent()
    data object OnPermissionDenied : Intent()
    data class OnImageSavedWith(val compositionContext: Context) : Intent()
    data object OnImageSavingCanceled : Intent()
    data class OnFinishPickingImagesWith(val compositionContext: Context, val imageUrls: List<Uri>) : Intent()
}