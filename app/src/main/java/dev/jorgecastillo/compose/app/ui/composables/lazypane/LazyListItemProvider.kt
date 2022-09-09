package dev.jorgecastillo.compose.app.ui.composables.lazypane

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.layout.LazyLayoutItemProvider

@ExperimentalFoundationApi
internal interface LazyListItemProvider : LazyLayoutItemProvider {
    /** The scope used by the item content lambdas */
    val itemScope: LazyItemScopeImpl
}
