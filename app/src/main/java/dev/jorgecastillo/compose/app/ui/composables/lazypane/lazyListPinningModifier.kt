package dev.jorgecastillo.compose.app.ui.composables.lazypane

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.layout.ModifierLocalPinnableParent
import androidx.compose.foundation.lazy.layout.PinnableParent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.ModifierLocalConsumer
import androidx.compose.ui.modifier.ModifierLocalProvider
import androidx.compose.ui.modifier.ModifierLocalReadScope
import androidx.compose.ui.modifier.ProvidableModifierLocal

/**
 * This is a temporary placeholder implementation of pinning until we implement b/195049010.
 */
@Suppress("ComposableModifierFactory")
@Composable
internal fun Modifier.lazyListPinningModifier(
    state: LazyListState,
    beyondBoundsInfo: LazyListBeyondBoundsInfo
): Modifier {
    return this then remember(state, beyondBoundsInfo) {
        LazyListPinningModifier(state, beyondBoundsInfo)
    }
}

@OptIn(ExperimentalFoundationApi::class)
private class LazyListPinningModifier(
    private val state: LazyListState,
    private val beyondBoundsInfo: LazyListBeyondBoundsInfo,
) : ModifierLocalProvider<PinnableParent?>, ModifierLocalConsumer, PinnableParent {
    var pinnableGrandParent: PinnableParent? = null

    override val key: ProvidableModifierLocal<PinnableParent?>
        get() = ModifierLocalPinnableParent

    override val value: PinnableParent
        get() = this

    override fun onModifierLocalsUpdated(scope: ModifierLocalReadScope) {
        pinnableGrandParent = with(scope) { ModifierLocalPinnableParent.current }
    }

    override fun pinItems(): PinnableParent.PinnedItemsHandle = with(beyondBoundsInfo) {
        if (hasIntervals()) {
            object : PinnableParent.PinnedItemsHandle {
                val parentPinnedItemsHandle = pinnableGrandParent?.pinItems()
                val interval = addInterval(start, end)
                override fun unpin() {
                    removeInterval(interval)
                    parentPinnedItemsHandle?.unpin()
                    state.remeasurement?.forceRemeasure()
                }
            }
        } else {
            pinnableGrandParent?.pinItems() ?: EmptyPinnedItemsHandle
        }
    }

    companion object {
        private val EmptyPinnedItemsHandle = object : PinnableParent.PinnedItemsHandle {
            override fun unpin() {}
        }
    }
}