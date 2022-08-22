package dev.jorgecastillo.compose.app

class NameRepository {

    private val names = listOf(
        "Jane Smith", "Aleesha Salgado", "Alayna Bradley", "Zunaira English", "Cassandra Higgins"
    )
    private var current = 0

    fun next(): String = names[current++ % names.size]
}
