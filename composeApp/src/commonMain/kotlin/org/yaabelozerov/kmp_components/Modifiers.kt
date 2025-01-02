package org.yaabelozerov.kmp_components

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.EaseOutQuart
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlinx.coroutines.delay

fun Modifier.scrollWithCap(
  scrollState: LazyListState, length: Dp, onLeft: () -> Unit = {}, onRight: () -> Unit = {}
) = composed(debugInspectorInfo {
  name = "length"
  value = length
}) {
  val color = MaterialTheme.colorScheme.primary
  var activeLeft by remember { mutableStateOf(false) }
  LaunchedEffect(scrollState.isScrollInProgress, scrollState.canScrollBackward) {
    if (scrollState.isScrollInProgress && !scrollState.canScrollBackward) {
      delay(300)
      activeLeft = true
    } else {
      if (activeLeft && !scrollState.isScrollInProgress) onLeft()
      activeLeft = false
    }
  }
  val progressLeft by animateFloatAsState(
    if (activeLeft) 1f else 0f, animationSpec = tween(durationMillis = 300, easing = EaseOutQuart)
  )

  var activeRight by remember { mutableStateOf(false) }
  LaunchedEffect(scrollState.isScrollInProgress, scrollState.canScrollForward) {
    if (scrollState.isScrollInProgress && !scrollState.canScrollForward) {
      delay(300)
      activeRight = true
    } else {
      if (activeRight && !scrollState.isScrollInProgress) onRight()
      activeRight = false
    }
  }
  val progressRight by animateFloatAsState(
    if (activeRight) -1f else 0f, animationSpec = tween(durationMillis = 300, easing = EaseOutQuart)
  )

  drawWithContent {
    val lengthValue = length.toPx()
    val progress = if (progressLeft > abs(progressRight)) progressLeft else progressRight
    translate(progress * lengthValue, 0f) { this@drawWithContent.drawContent() }
    drawCircle(
      color.copy(alpha = progressLeft),
      progressLeft * lengthValue / 4,
      Offset(progressLeft * lengthValue / 2, size.height / 2)
    )
    drawCircle(
      color.copy(alpha = -progressRight),
      -progressRight * lengthValue / 4,
      Offset(progressRight * lengthValue / 2 + size.width, size.height / 2)
    )
  }
}

fun Modifier.horizontalFadingEdge(
  scrollState: ScrollState,
  length: Dp,
  edgeColor: Color? = null,
) = composed(debugInspectorInfo {
  name = "length"
  value = length
}) {
  val color = edgeColor ?: MaterialTheme.colorScheme.surface
  drawWithContent {
    val lengthValue = length.toPx()
    val scrollFromStart = scrollState.value
    val scrollFromEnd = scrollState.maxValue - scrollState.value
    val startFadingEdgeStrength = lengthValue * (scrollFromStart / lengthValue).coerceAtMost(1f)
    val endFadingEdgeStrength = lengthValue * (scrollFromEnd / lengthValue).coerceAtMost(1f)
    drawContent()
    drawRect(
      brush = Brush.horizontalGradient(
        colors = listOf(
          color,
          Color.Transparent,
        ),
        startX = 0f,
        endX = startFadingEdgeStrength,
      ),
      size = Size(
        startFadingEdgeStrength,
        this.size.height,
      ),
    )
    drawRect(
      brush = Brush.horizontalGradient(
        colors = listOf(
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
) = composed(debugInspectorInfo {
  name = "length"
  value = length
}) {
  val color = edgeColor ?: MaterialTheme.colorScheme.surface
  val startFadingEdge by animateDpAsState(if (lazyListState.canScrollBackward) length else 0.dp)
  val endFadingEdge by animateDpAsState(if (lazyListState.canScrollForward) length else 0.dp)

  drawWithContent {
    drawContent()
    drawRect(
      brush = Brush.horizontalGradient(
        colors = listOf(
          color,
          Color.Transparent,
        ),
        startX = 0f,
        endX = startFadingEdge.toPx(),
      ),
      size = Size(
        startFadingEdge.toPx(),
        this.size.height,
      ),
    )
    drawRect(
      brush = Brush.horizontalGradient(
        colors = listOf(
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

  val translateAnimation by transition.animateFloat(
    initialValue = 0f,
    targetValue = 400f,
    animationSpec = infiniteRepeatable(
      tween(durationMillis = 2000, easing = EaseInOut), RepeatMode.Restart
    ),
    label = "",
  )
  val shimmerColors = listOf(
    MaterialTheme.colorScheme.surfaceBright.copy(alpha = 0.7f),
    MaterialTheme.colorScheme.surfaceBright.copy(alpha = 0.0f),
  )
  val brush = Brush.linearGradient(
    colors = shimmerColors,
    start = Offset(translateAnimation, translateAnimation),
    end = Offset(translateAnimation + 200f, translateAnimation + 200f),
    tileMode = TileMode.Mirror,
  )
  return@composed this.then(background(brush, shape))
}

fun Modifier.bouncyClickable(onClick: () -> Unit = {}, shrinkSize: Float = 0.95f) = composed {
  var buttonState by remember { mutableStateOf(false) }
  val scale by animateFloatAsState(if (buttonState) shrinkSize else 1f)
  graphicsLayer {
    scaleX = scale
    scaleY = scale
  }.clickable(interactionSource = remember { MutableInteractionSource() },
      indication = null,
      onClick = { onClick() }).pointerInput(buttonState) {
      awaitPointerEventScope {
        buttonState = if (buttonState) {
          waitForUpOrCancellation()
          false
        } else {
          awaitFirstDown(false)
          true
        }
      }
    }
}

fun Modifier.bouncyClickable(shrinkSize: Float = 0.95f) = composed {
  var buttonState by remember { mutableStateOf(false) }
  val scale by animateFloatAsState(if (buttonState) shrinkSize else 1f)
  graphicsLayer {
    scaleX = scale
    scaleY = scale
  }.pointerInput(buttonState) {
      awaitPointerEventScope {
        buttonState = if (buttonState) {
          waitForUpOrCancellation()
          false
        } else {
          awaitFirstDown(false)
          true
        }
      }
    }
}
