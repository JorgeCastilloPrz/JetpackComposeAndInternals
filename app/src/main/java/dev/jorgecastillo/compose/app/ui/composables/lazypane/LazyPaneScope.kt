package dev.jorgecastillo.compose.app.ui.composables.lazypane

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyScopeMarker
import androidx.compose.runtime.Composable

/**
 * Receiver scope which is used by [LazyPane].
 */
@LazyScopeMarker
interface LazyPaneScope {

    /**
     * Adds a [count] of items.
     *
     * @param count the items count
     * @param key a factory of stable and unique keys representing the item. Using the same key
     * for multiple items in the list is not allowed. Type of the key should be saveable
     * via Bundle on Android. If null is passed the position in the list will represent the key.
     * When you specify the key the scroll position will be maintained based on the key, which
     * means if you add/remove items before the current visible item the item with the given key
     * will be kept as the first visible one.
     * @param contentType a factory of the content types for the item. The item compositions of
     * the same type could be reused more efficiently. Note that null is a valid type and items of such
     * type will be considered compatible.
     * @param itemContent the content displayed by a single item
     */
    fun items(
        count: Int,
        key: ((indexRow: Int, indexColumn: Int) -> Any)? = null,
        contentType: (indexRow: Int, indexColumn: Int) -> Any? = { _, _ -> null },
        itemContent: @Composable LazyItemScope.(indexRow: Int, indexColumn: Int) -> Unit
    ) {
        error("The method is not implemented")
    }
}

/**
 * Adds a list of items.
 *
 * @param items the data list
 * @param key a factory of stable and unique keys representing the item. Using the same key
 * for multiple items in the list is not allowed. Type of the key should be saveable
 * via Bundle on Android. If null is passed the position in the list will represent the key.
 * When you specify the key the scroll position will be maintained based on the key, which
 * means if you add/remove items before the current visible item the item with the given key
 * will be kept as the first visible one.
 * @param contentType a factory of the content types for the item. The item compositions of
 * the same type could be reused more efficiently. Note that null is a valid type and items of such
 * type will be considered compatible.
 * @param itemContent the content displayed by a single item
 */
inline fun <T> LazyPaneScope.items(
    items: List<List<T>>,
    noinline key: ((item: T) -> Any)? = null,
    noinline contentType: (item: T) -> Any? = { null },
    crossinline itemContent: @Composable LazyItemScope.(item: T) -> Unit
) = items(
    count = items.size,
    key = if (key != null) { indexRow: Int, indexColumn: Int -> key(items[indexRow][indexColumn]) } else null,
    contentType = { indexRow: Int, indexColumn: Int -> contentType(items[indexRow][indexColumn]) }
) { indexRow, indexColumn ->
    itemContent(items[indexRow][indexColumn])
}
