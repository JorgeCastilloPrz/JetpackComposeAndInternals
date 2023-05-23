@file:OptIn(ExperimentalFoundationApi::class)

package dev.jorgecastillo.compose.app.ui.composables.lazylayout

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.layout.LazyLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.Constraints

@Composable
fun CustomLazyLayout(
    modifier: Modifier = Modifier,
    state: LazyLayoutState = rememberLazyLayoutState(),
    content: CustomLazyListScope.() -> Unit
) {
    val itemProvider = rememberItemProvider(content)

    LazyLayout(
        modifier = modifier
            .clipToBounds()
            .lazyLayoutPointerInput(state),
        itemProvider = itemProvider,
    ) { constraints ->
        val boundaries = state.getBoundaries(constraints)
        val indexes = itemProvider.getItemIndexesInRange(boundaries)

        val indexesWithPlaceables = indexes.associateWith {
            measure(it, Constraints())
        }

        layout(constraints.maxWidth, constraints.maxHeight) {
            indexesWithPlaceables.forEach { (index, placeables) ->
                val item = itemProvider.getItem(index)
                item?.let { placeItem(state, item, placeables) }
            }
        }
    }
}

private fun Placeable.PlacementScope.placeItem(
    state: LazyLayoutState,
    listItem: ListItem,
    placeables: List<Placeable>
) {
    val xPosition = listItem.x - state.offsetState.value.x
    val yPosition = listItem.y - state.offsetState.value.y

    placeables.forEach { placeable ->
        placeable.placeRelative(xPosition, yPosition)
    }
}

/**
 * Custom item provider implementation for the lazy layout. The [customLazyListScope] lambda param
 * is the content lambda that user will pass when using our lazy layout. That's why it runs in the
 * scope of [CustomLazyListScope] (receiver). That lambda is remembered state so every time it
 * changes it will cause our lazy layout to recompose.
 */
@Composable
fun rememberItemProvider(customLazyListScope: CustomLazyListScope.() -> Unit): ItemProvider {
    val customLazyListScopeState = remember { mutableStateOf(customLazyListScope) }.apply {
        value = customLazyListScope
    }

    return remember {
        ItemProvider(
            derivedStateOf {
                val layoutScope = CustomLazyListScopeImpl().apply(customLazyListScopeState.value)
                layoutScope.items // trigger recomposition every time items change
            }
        )
    }
}