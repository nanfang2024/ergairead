package io.legado.app.ui.widget.compose.ng

import android.graphics.drawable.Drawable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.ngJellyBackground(): Modifier {
    val palette = LocalNgGlassPalette.current
    return drawWithCache {
        val span = maxOf(size.width, size.height)
        fun blob(color: Color, centerX: Float, centerY: Float, radiusFactor: Float): Brush =
            Brush.radialGradient(
                colors = listOf(color, color.copy(alpha = 0f)),
                center = Offset(centerX, centerY),
                radius = span * radiusFactor
            )
        val topBlob = blob(palette.blobPrimary, size.width * 0.88f, -span * 0.10f, 0.68f)
        val midBlob = blob(palette.blobSecondary, -span * 0.08f, size.height * 0.32f, 0.60f)
        val lowBlob = blob(palette.blobTertiary, size.width * 1.04f, size.height * 0.88f, 0.62f)
        onDrawBehind {
            drawRect(palette.pageBase)
            drawRect(topBlob)
            drawRect(midBlob)
            drawRect(lowBlob)
        }
    }
}

@Composable
fun Modifier.ngGlassPanel(
    radiusPx: Float,
    overlayImage: Drawable? = null
): Modifier {
    val palette = LocalNgGlassPalette.current
    return drawWithCache {
        val strokeWidth = 1.dp.toPx()
        val inset = strokeWidth / 2f
        val outlinePath = Path().apply {
            addRoundRect(
                RoundRect(
                    rect = Rect(inset, inset, size.width - inset, size.height - inset),
                    cornerRadius = CornerRadius(radiusPx, radiusPx)
                )
            )
        }
        val fillBrush = Brush.verticalGradient(
            0f to palette.panelFillTop,
            1f to palette.panelFillBottom
        )
        val borderBrush = Brush.verticalGradient(
            0f to palette.edgeLight,
            0.55f to palette.edgeLight.copy(alpha = palette.edgeLight.alpha * 0.30f),
            1f to palette.edgeShade
        )
        val glareWidth = 1.4.dp.toPx()
        val glareTopLeft = Offset(size.width * 0.16f, inset + size.height * 0.06f)
        val glareSize = Size(size.width * 0.68f, size.height * 0.42f)
        onDrawBehind {
            drawPath(outlinePath, fillBrush)
            drawPath(outlinePath, borderBrush, style = Stroke(width = strokeWidth))
            drawArc(
                color = palette.glare,
                startAngle = 187f,
                sweepAngle = 166f,
                useCenter = false,
                topLeft = glareTopLeft,
                size = glareSize,
                style = Stroke(width = glareWidth, cap = StrokeCap.Round)
            )
            overlayImage?.let { drawable ->
                drawable.setBounds(0, 0, size.width.toInt(), size.height.toInt())
                drawIntoCanvas { canvas ->
                    drawable.draw(canvas.nativeCanvas)
                }
            }
        }
    }
}
