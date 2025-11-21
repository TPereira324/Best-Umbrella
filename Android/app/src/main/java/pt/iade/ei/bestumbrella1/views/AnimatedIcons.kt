package pt.iade.ei.bestumbrella1.views

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness3
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.min
import kotlin.random.Random

@Composable
fun WeatherAnimatedIcon(isRaining: Boolean, isCloudy: Boolean, isNight: Boolean) {
    val size = 80.dp
    when {
        isRaining -> RainAnimatedIcon(size)
        isNight -> MoonAnimatedIcon(size)
        isCloudy -> CloudAnimatedIcon(size)
        else -> SunAnimatedIcon(size)
    }
}

@Composable
fun SunAnimatedIcon(size: Dp) {
    val transition = rememberInfiniteTransition(label = "sun")
    val rotation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "sun-rot"
    )
    val scale by transition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "sun-scale"
    )
    Icon(
        Icons.Default.WbSunny,
        contentDescription = null,
        tint = Color(0xFFFFC107),
        modifier = Modifier
            .size(size)
            .rotate(rotation)
            .scale(scale)
    )
}

@Composable
fun CloudAnimatedIcon(size: Dp) {
    val transition = rememberInfiniteTransition(label = "cloud")
    val dx by transition.animateFloat(
        initialValue = -6f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "cloud-dx"
    )
    Box(modifier = Modifier.size(size)) {
        Icon(
            Icons.Default.Cloud,
            contentDescription = null,
            tint = Color(0xFF90A4AE),
            modifier = Modifier
                .fillMaxSize()
                .offset(x = dx.dp)
        )
    }
}

@Composable
fun RainAnimatedIcon(sizeDp: Dp) {
    val transition = rememberInfiniteTransition(label = "rain")
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rain-progress"
    )
    val drops = remember {
        List(14) { Random.nextFloat() }
    }
    Box(modifier = Modifier.size(sizeDp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = this.size.width
            val h = this.size.height
            val cloudBottomY = h * 0.48f
            val dropLen = h * 0.22f
            val strokeWidth = min(w, h) * 0.02f
            drops.forEachIndexed { idx, xRel ->
                val x = xRel * w
                val travel = (h - cloudBottomY) + dropLen
                val yStart = (progress * travel - idx * (dropLen / 3)) % travel + cloudBottomY
                val start = Offset(x, yStart - dropLen)
                val end = Offset(x, yStart)
                drawLine(
                    color = Color(0xFF2196F3),
                    start = start,
                    end = end,
                    strokeWidth = strokeWidth
                )
            }
        }
        Icon(
            Icons.Default.Cloud,
            contentDescription = null,
            tint = Color(0xFF90A4AE),
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun MoonAnimatedIcon(size: Dp) {
    val transition = rememberInfiniteTransition(label = "moon")
    val scale by transition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "moon-scale"
    )
    Icon(
        Icons.Default.Brightness3,
        contentDescription = null,
        tint = Color(0xFF1976D2),
        modifier = Modifier
            .size(size)
            .scale(scale)
    )
}