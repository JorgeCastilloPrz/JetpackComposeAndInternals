package dev.jorgecastillo.compose.app.ui.composables.lazylayout

/**
 * The scope for the custom lazy layout content lambda. It provides the [items] function to pass
 * a list of items to display lazily, so only the ones visible on screen will be composed.
 */
interface CustomLazyListScope {
    fun items(items: List<ListItem>, itemContent: ComposableItemContent)
}
