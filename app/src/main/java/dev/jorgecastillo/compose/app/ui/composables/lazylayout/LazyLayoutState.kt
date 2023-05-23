package dev.jorgecastillo.compose.app.ui.composables.lazylayout

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset

@Composable
fun rememberLazyLayoutState(): LazyLayoutState {
    return remember { LazyLayoutState() }
}


/**
 * When building a custom lazy layout, we have to handle scrolling by ourselves. To achieve this,
 * some state which holds a scroll position is necessary. We also need to get the boundaries which
 * represent what area is visible in order to be able to draw only needed items.
 */
@Stable
class LazyLayoutState {

    // this represents the scroll position
    private val _offsetState = mutableStateOf(IntOffset(0, 0))
    val offsetState = _offsetState

    /**
     * Updates scroll position (_offsetState).
     */
    fun onDrag(offset: IntOffset) {
        val x = (_offsetState.value.x - offset.x).coerceAtLeast(0)
        val y = (_offsetState.value.y - offset.y).coerceAtLeast(0)
        _offsetState.value = IntOffset(x, y)
    }

    /**
     *  Returns the visible area based on scroll position (offset) and constraints of the layout.
     *  This area will be used to draw only the needed items.
     */
    fun getBoundaries(
        constraints: Constraints,
        threshold: Int = 500
    ): ViewBoundaries {
        return ViewBoundaries(
            fromX = offsetState.value.x - threshold,
            toX = constraints.maxWidth + offsetState.value.x + threshold,
            fromY = offsetState.value.y - threshold,
            toY = constraints.maxHeight + offsetState.value.y + threshold
        )
    }
}

/**
 * This modifier updates the scroll position in the lazy layout state. So we will be able to place
 * only the items we need and place them in the correct position.
 */
fun Modifier.lazyLayoutPointerInput(state: LazyLayoutState): Modifier {
    return pointerInput(Unit) {
        detectDragGestures { change, dragAmount ->
            change.consume()
            state.onDrag(IntOffset(dragAmount.x.toInt(), dragAmount.y.toInt()))
        }
    }
}
