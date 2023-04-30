package dev.jorgecastillo.compose.app

class ReadWriteCounter {

    private var reads: Int = 0
    private var writes: Int = 0

    fun trackRead() {
        reads++
    }

    fun trackWrite() {
        writes++
    }

    fun reads() = reads
    fun writes() = writes
}