package org.yaabelozerov.kmp_components

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.horizontalFadingEdge(
    scrollState: ScrollState,
    length: Dp,
    edgeColor: Color? = null,
) =
    composed(
        debugInspectorInfo {
          name = "length"
          value = length
        }) {
          val color = edgeColor ?: MaterialTheme.colorScheme.surface
          drawWithContent {
            val lengthValue = length.toPx()
            val scrollFromStart = scrollState.value
            val scrollFromEnd = scrollState.maxValue - scrollState.value
            val startFadingEdgeStrength =
                lengthValue * (scrollFromStart / lengthValue).coerceAtMost(1f)
            val endFadingEdgeStrength = lengthValue * (scrollFromEnd / lengthValue).coerceAtMost(1f)
            drawContent()
            drawRect(
                brush =
                    Brush.horizontalGradient(
                        colors =
                            listOf(
                                color,
                                Color.Transparent,
                            ),
                        startX = 0f,
                        endX = startFadingEdgeStrength,
                    ),
                size =
                    Size(
                        startFadingEdgeStrength,
                        this.size.height,
                    ),
            )
            drawRect(
                brush =
                    Brush.horizontalGradient(
                        colors =
                            listOf(
                                Color.Transparent,
                                color,
                            ),
                        startX = size.width - endFadingEdgeStrength,
                        endX = size.width,
                    ),
                topLeft = Offset(x = size.width - endFadingEdgeStrength, y = 0f),
            )
          }
        }

fun Modifier.horizontalFadingEdge(
    lazyListState: LazyListState,
    length: Dp,
    edgeColor: Color? = null,
) =
    composed(
        debugInspectorInfo {
          name = "length"
          value = length
        }) {
          val color = edgeColor ?: MaterialTheme.colorScheme.surface
          val startFadingEdge by
              animateDpAsState(if (lazyListState.canScrollBackward) length else 0.dp)
          val endFadingEdge by
              animateDpAsState(if (lazyListState.canScrollForward) length else 0.dp)

          drawWithContent {
            drawContent()
            drawRect(
                brush =
                    Brush.horizontalGradient(
                        colors =
                            listOf(
                                color,
                                Color.Transparent,
                            ),
                        startX = 0f,
                        endX = startFadingEdge.toPx(),
                    ),
                size =
                    Size(
                        startFadingEdge.toPx(),
                        this.size.height,
                    ),
            )
            drawRect(
                brush =
                    Brush.horizontalGradient(
                        colors =
                            listOf(
                                Color.Transparent,
                                color,
                            ),
                        startX = size.width - endFadingEdge.toPx(),
                        endX = size.width,
                    ),
                topLeft = Offset(x = size.width - endFadingEdge.toPx(), y = 0f),
            )
          }
        }

fun Modifier.shimmerBackground(shape: Shape = RectangleShape): Modifier = composed {
  val transition = rememberInfiniteTransition()

  val translateAnimation by
      transition.animateFloat(
          initialValue = 0f,
          targetValue = 400f,
          animationSpec =
              infiniteRepeatable(
                  tween(durationMillis = 2000, easing = EaseInOut), RepeatMode.Restart),
          label = "",
      )
  val shimmerColors =
      listOf(
          MaterialTheme.colorScheme.surfaceBright.copy(alpha = 0.7f),
          MaterialTheme.colorScheme.surfaceBright.copy(alpha = 0.0f),
      )
  val brush =
      Brush.linearGradient(
          colors = shimmerColors,
          start = Offset(translateAnimation, translateAnimation),
          end = Offset(translateAnimation + 200f, translateAnimation + 200f),
          tileMode = TileMode.Mirror,
      )
  return@composed this.then(background(brush, shape))
}
