package dev.jorgecastillo.compose.app

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.Snapshot
import org.junit.Test

/**
 * Showcases how snapshots work for reading and propagating changes.
 */
class SnapshotStateTest {

    @Test
    fun `entering a snapshot`() {
        var name by mutableStateOf("")
        name = "Aleesha Salgado"
        val snapshot = Snapshot.takeSnapshot()
        name = "Jessica Jones"

        println(name) // Jessica Jones
        snapshot.enter { println(name) } // Aleesha Salgado
        println(name) // Jessica Jones
    }

    @Test
    fun `applying snapshot changes`() {
        var name by mutableStateOf("")
        name = "Aleesha Salgado"
        val snapshot = Snapshot.takeMutableSnapshot()

        snapshot.enter { name = "Jessica Jones" }
        println(name) // Aleesha Salgado

        snapshot.apply() // propagate changes

        println(name) // Jessica Jones
    }

    @Test
    fun `nested snapshots`() {
        var name by mutableStateOf("")
        name = "Aleesha Salgado"

        val first = Snapshot.takeMutableSnapshot()
        first.enter {
            name = "Jessica Jones"

            val second = Snapshot.takeMutableSnapshot()
            second.enter {
                name = "Cassandra Higgins"
            }
            println("before second.apply(): $name") // Jessica Jones
            second.apply()
            println("after second.apply(): $name") // Cassandra Higgins
        }
        println("before first.apply(): $name") // Aleesha Salgado
        first.apply()
        println("after first.apply(): $name") // Cassandra Higgins
    }
}
