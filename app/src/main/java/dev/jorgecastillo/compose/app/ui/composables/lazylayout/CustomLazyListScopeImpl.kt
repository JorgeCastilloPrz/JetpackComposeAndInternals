package dev.jorgecastillo.compose.app.ui.composables.lazylayout

/**
 * Ergonomic api for the scope so we don't need to pass LazyLayoutItemContent when creating the
 * lazy layout, but just the actual list of items. The wrapping is done here internally.
 */
class CustomLazyListScopeImpl() : CustomLazyListScope {

    private val _items = mutableListOf<LazyLayoutItemContent>()
    val items: List<LazyLayoutItemContent> = _items

    override fun items(items: List<ListItem>, itemContent: ComposableItemContent) {
        items.forEach { _items.add(LazyLayoutItemContent(it, itemContent)) }
    }
}
