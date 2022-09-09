package dev.jorgecastillo.compose.app.ui.composables.lazypane

@kotlin.jvm.JvmInline
internal value class DataIndex(val value: Int) {
    inline operator fun inc(): DataIndex = DataIndex(value + 1)
    inline operator fun dec(): DataIndex = DataIndex(value - 1)
    inline operator fun plus(i: Int): DataIndex = DataIndex(value + i)
    inline operator fun minus(i: Int): DataIndex = DataIndex(value - i)
    inline operator fun minus(i: DataIndex): DataIndex = DataIndex(value - i.value)
    inline operator fun compareTo(other: DataIndex): Int = value - other.value
}