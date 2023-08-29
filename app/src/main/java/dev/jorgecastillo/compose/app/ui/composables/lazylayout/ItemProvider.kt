@file:OptIn(ExperimentalFoundationApi::class)

package dev.jorgecastillo.compose.app.ui.composables.lazylayout

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.layout.LazyLayoutItemProvider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State

/**
 * Custom [LazyLayoutItemProvider] for our list of [LazyLayoutItemContent]. The purpose of a lazy
 * layout item provider is to provide the items that will later be composed and displayed.
 */
class ItemProvider constructor(
    private val itemsState: State<List<LazyLayoutItemContent>>
) :
    LazyLayoutItemProvider {
    override val itemCount
        get() = itemsState.value.size

    @Composable
    override fun Item(index: Int, key: Any) {
        val item = itemsState.value.getOrNull(index)
        item?.itemContent?.invoke(item.item)
    }

    fun getItemIndexesInRange(boundaries: ViewBoundaries): List<Int> {
        val result = mutableListOf<Int>()

        itemsState.value.forEachIndexed { index, itemContent ->
            val listItem = itemContent.item
            if (listItem.x in boundaries.fromX..boundaries.toX &&
                listItem.y in boundaries.fromY..boundaries.toY
            ) {
                result.add(index)
            }
        }

        return result
    }

    fun getItem(index: Int): ListItem? {
        return itemsState.value.getOrNull(index)?.item
    }
}
