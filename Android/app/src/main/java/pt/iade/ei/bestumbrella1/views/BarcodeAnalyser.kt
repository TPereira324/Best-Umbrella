package pt.iade.ei.bestumbrella1.views

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.BarcodeFormat
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.DecodeHintType

class BarcodeAnalyser(
    private val onCodeDetected: (String) -> Unit
) : ImageAnalysis.Analyzer {
    private val reader = MultiFormatReader().apply {
        val hints = mapOf(
            DecodeHintType.POSSIBLE_FORMATS to listOf(BarcodeFormat.QR_CODE)
        )
        setHints(hints)
    }

    override fun analyze(image: ImageProxy) {
        try {
            val width = image.width
            val height = image.height

            val yPlane = image.planes[0]
            val yBuffer = yPlane.buffer
            val yRowStride = yPlane.rowStride
            val yPixelStride = yPlane.pixelStride

            val luminance = ByteArray(width * height)
            var offset = 0

            if (yPixelStride == 1 && yRowStride == width) {
                yBuffer.get(luminance, 0, width * height)
            } else {
                val position = yBuffer.position()
                for (row in 0 until height) {
                    var colOffset = row * yRowStride
                    for (col in 0 until width) {
                        luminance[offset++] = yBuffer.get(colOffset + col * yPixelStride)
                    }
                }
                yBuffer.position(position)
            }

            val source = PlanarYUVLuminanceSource(
                luminance,
                width,
                height,
                0,
                0,
                width,
                height,
                false
            )

            val bitmap = BinaryBitmap(HybridBinarizer(source))
            val result = reader.decode(bitmap)
            val text = result.text
            if (!text.isNullOrEmpty()) {
                onCodeDetected(text)
            }
            reader.reset()
        } catch (_: Exception) {
        } finally {
            image.close()
        }
    }
}