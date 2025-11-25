package pt.iade.ei.bestumbrella1.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

fun umbrellaMarkerIcon(context: Context, available: Boolean): BitmapDescriptor {
    val density = context.resources.displayMetrics.density
    val sizePx = (48 * density).toInt()
    val bitmap = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = (if (available) Color(0xFF1976D2) else Color(0xFF9E9E9E)).toArgb()
    }

    val radius = sizePx / 2f
    canvas.drawCircle(radius, radius, radius, bgPaint)

    val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = android.graphics.Color.WHITE
        textAlign = Paint.Align.CENTER
        textSize = sizePx * 0.6f
    }
    val fm = textPaint.fontMetrics
    val textCenterY = sizePx / 2f - (fm.ascent + fm.descent) / 2f
    canvas.drawText("☂", sizePx / 2f, textCenterY, textPaint)

    return BitmapDescriptorFactory.fromBitmap(bitmap)
}

