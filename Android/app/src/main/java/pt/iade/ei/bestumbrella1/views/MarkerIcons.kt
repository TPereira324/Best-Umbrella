package pt.iade.ei.bestumbrella1.views.map

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng

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

fun distanceKm(a: LatLng, b: LatLng): Double {
    val R = 6371.0
    val dLat = Math.toRadians(b.latitude - a.latitude)
    val dLon = Math.toRadians(b.longitude - a.longitude)
    val lat1 = Math.toRadians(a.latitude)
    val lat2 = Math.toRadians(b.latitude)
    val aa =
        Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(lat1) * Math.cos(lat2) * Math.sin(
            dLon / 2
        ) * Math.sin(dLon / 2)
    val c = 2 * Math.atan2(Math.sqrt(aa), Math.sqrt(1 - aa))
    return R * c
}