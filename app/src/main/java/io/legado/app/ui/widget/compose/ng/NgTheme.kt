package io.legado.app.ui.widget.compose.ng

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import io.legado.app.lib.theme.backgroundColor
import io.legado.app.lib.theme.rememberThemeUiPalette
import io.legado.app.utils.ColorUtils

@Immutable
data class NgGlassPalette(
    val pageBase: Color,
    val blobPrimary: Color,
    val blobSecondary: Color,
    val blobTertiary: Color,
    val panelFillTop: Color,
    val panelFillBottom: Color,
    val edgeLight: Color,
    val edgeShade: Color,
    val glare: Color,
    val isDark: Boolean,
    val signature: String
)

val LocalNgGlassPalette = staticCompositionLocalOf { defaultNgGlassPalette() }

fun defaultNgGlassPalette(): NgGlassPalette = lightNgGlassPalette(
    pageBase = Color(0xFFF6F1F7),
    signature = "ng-default"
)

fun lightNgGlassPalette(pageBase: Color, signature: String): NgGlassPalette = NgGlassPalette(
    pageBase = pageBase,
    blobPrimary = Color(0xFFFFB7CE).copy(alpha = 0.50f),
    blobSecondary = Color(0xFFC9B6F6).copy(alpha = 0.44f),
    blobTertiary = Color(0xFF9FE3D6).copy(alpha = 0.44f),
    panelFillTop = Color.White.copy(alpha = 0.58f),
    panelFillBottom = Color.White.copy(alpha = 0.30f),
    edgeLight = Color.White.copy(alpha = 0.90f),
    edgeShade = Color(0xFF3A3A5C).copy(alpha = 0.18f),
    glare = Color.White.copy(alpha = 0.65f),
    isDark = false,
    signature = signature
)

fun darkNgGlassPalette(pageBase: Color, signature: String): NgGlassPalette = NgGlassPalette(
    pageBase = pageBase,
    blobPrimary = Color(0xFFFFB7CE).copy(alpha = 0.16f),
    blobSecondary = Color(0xFFC9B6F6).copy(alpha = 0.14f),
    blobTertiary = Color(0xFF9FE3D6).copy(alpha = 0.13f),
    panelFillTop = Color.White.copy(alpha = 0.13f),
    panelFillBottom = Color.White.copy(alpha = 0.05f),
    edgeLight = Color.White.copy(alpha = 0.22f),
    edgeShade = Color.Black.copy(alpha = 0.40f),
    glare = Color.White.copy(alpha = 0.24f),
    isDark = true,
    signature = signature
)

@Composable
fun rememberNgGlassPalette(): NgGlassPalette {
    val context = LocalContext.current
    val themePalette = rememberThemeUiPalette()
    val pageBase = Color(context.backgroundColor)
    val isDark = !ColorUtils.isColorLight(themePalette.cardColor)
    return remember(pageBase, isDark, themePalette.signature) {
        if (isDark) {
            darkNgGlassPalette(pageBase, themePalette.signature)
        } else {
            lightNgGlassPalette(pageBase, themePalette.signature)
        }
    }
}
