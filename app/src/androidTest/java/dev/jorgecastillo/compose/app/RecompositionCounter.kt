package dev.jorgecastillo.compose.app

import androidx.compose.runtime.Immutable

@Immutable
class RecompositionCounter {
    private var count = 0

    fun increment() {
        count++
    }

    fun count() = count
    fun reset() {
        count = 0
    }
}
