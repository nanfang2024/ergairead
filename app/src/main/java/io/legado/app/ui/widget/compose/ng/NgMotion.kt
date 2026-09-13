package io.legado.app.ui.widget.compose.ng

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer

const val NgJellyPressedScale = 0.96f

val NgJellySpring = spring<Float>(
    dampingRatio = 0.45f,
    stiffness = 500f,
    visibilityThreshold = 0.001f
)

@Composable
fun Modifier.ngJellyPress(
    pressed: Boolean,
    pressedScale: Float = NgJellyPressedScale
): Modifier {
    val scale by animateFloatAsState(
        targetValue = if (pressed) pressedScale else 1f,
        animationSpec = NgJellySpring,
        label = "ngJellyPress"
    )
    return graphicsLayer {
        scaleX = scale
        scaleY = scale
    }
}
