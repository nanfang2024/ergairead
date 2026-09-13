package io.legado.app.ui.main.bookshelf.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.legado.app.R
import io.legado.app.help.config.AppConfig
import io.legado.app.lib.theme.accentColor
import io.legado.app.lib.theme.titleTextColor
import io.legado.app.ui.widget.compose.ng.LocalNgGlassPalette
import io.legado.app.ui.widget.compose.ng.ngJellyPress

@Composable
fun NgShelfFilterBar(
    filter: NgShelfFilter,
    onFilterSelect: (NgShelfFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val accent = remember(context) { Color(context.accentColor) }
    val contentColor = remember(context) { Color(context.titleTextColor) }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 8.dp, vertical = 2.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        NgShelfFilter.entries.forEach { candidate ->
            NgShelfFilterChip(
                filter = candidate,
                selected = candidate == filter,
                accent = accent,
                contentColor = contentColor,
                onClick = { onFilterSelect(candidate) }
            )
        }
    }
}

@Composable
private fun NgShelfFilterChip(
    filter: NgShelfFilter,
    selected: Boolean,
    accent: Color,
    contentColor: Color,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val jellyPress = pressed && !AppConfig.isEInkMode
    Text(
        text = stringResource(filter.labelRes),
        color = if (selected) Color.White else contentColor,
        fontSize = 13.sp,
        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier
            .ngJellyPress(jellyPress, pressedScale = 0.93f)
            .clip(RoundedCornerShape(50))
            .then(
                if (selected) {
                    Modifier.background(
                        Brush.verticalGradient(
                            0f to accent.copy(alpha = 0.94f),
                            1f to accent.copy(alpha = 0.76f)
                        )
                    )
                } else {
                    Modifier.ngGlassChipSurface()
                }
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 14.dp, vertical = 7.dp)
    )
}

@Composable
private fun Modifier.ngGlassChipSurface(): Modifier {
    val palette = LocalNgGlassPalette.current
    return drawBehind {
        val corner = CornerRadius(size.height / 2f)
        drawRoundRect(
            brush = Brush.verticalGradient(
                0f to palette.panelFillTop,
                1f to palette.panelFillBottom
            ),
            cornerRadius = corner
        )
        drawRoundRect(
            brush = Brush.verticalGradient(
                0f to palette.edgeLight,
                1f to palette.edgeShade
            ),
            cornerRadius = corner,
            style = Stroke(width = 1.dp.toPx())
        )
    }
}

@Composable
fun NgMangaBadge(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.ng_media_manga),
        color = Color.White,
        fontSize = 9.sp,
        fontWeight = FontWeight.SemiBold,
        maxLines = 1,
        modifier = modifier
            .clip(RoundedCornerShape(topStart = 4.dp, bottomEnd = 4.dp))
            .background(
                Brush.verticalGradient(
                    0f to Color.Black.copy(alpha = 0.46f),
                    1f to Color.Black.copy(alpha = 0.30f)
                )
            )
            .padding(horizontal = 5.dp, vertical = 2.dp)
    )
}
