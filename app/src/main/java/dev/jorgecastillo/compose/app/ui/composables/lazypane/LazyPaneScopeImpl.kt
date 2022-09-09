package dev.jorgecastillo.compose.app.ui.composables.lazypane

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.runtime.Composable

@OptIn(ExperimentalFoundationApi::class)
internal class LazyPaneScopeImpl : LazyPaneScope {

    private val _intervals = MutableIntervalList<LazyListIntervalContent>()
    val intervals: IntervalList<LazyListIntervalContent> = _intervals

    override fun items(
        count: Int,
        key: ((indexRow: Int, indexColumn: Int) -> Any)?,
        contentType: (indexRow: Int, indexColumn: Int) -> Any?,
        itemContent: @Composable LazyItemScope.(indexRow: Int, indexColumn: Int) -> Unit
    ) {
        _intervals.addInterval(
            count,
            LazyListIntervalContent(
                key = key,
                type = contentType,
                item = itemContent
            )
        )
    }
}

internal class LazyListIntervalContent(
    val key: ((indexRow: Int, indexColumn: Int) -> Any)?,
    val type: (indexRow: Int, indexColumn: Int) -> Any?,
    val item: @Composable LazyItemScope.(indexRow: Int, indexColumn: Int) -> Unit
)
