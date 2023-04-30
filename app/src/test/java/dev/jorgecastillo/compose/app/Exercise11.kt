package dev.jorgecastillo.compose.app

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.MutableSnapshot
import androidx.compose.runtime.snapshots.Snapshot
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

/**
 * ### Exercise 11 👩🏾‍💻
 *
 * This exercise helps us to grasp how [Snapshot] works in Compose.
 *
 * Instructions to solve every exercise added on top of each test below.
 */
class Exercise11 {

    /**
     * 1. Implement [takeReadOnlySnapshot] to take a snapshot that will only be used to read state
     *    objects.
     *
     * 2. Run the test. Since we are taking the screenshot right after setting the name to "Aleesha
     *    Salgado", that is the value the name should have when entering the snapshot. Outside of
     *    it, it will stay as "Jessica Jones" (see the assertions).
     */
    @Test
    fun `read state in the snapshot context`() {
        var name by mutableStateOf("")
        name = "Aleesha Salgado"
        val snapshot = takeReadOnlySnapshot()
        name = "Jessica Jones"

        assertThat(name, `is`("Jessica Jones"))
        snapshot.enter { assertThat(name, `is`("Aleesha Salgado")) }
        assertThat(name, `is`("Jessica Jones"))
    }

    /**
     * 1. Implement [takeMutableSnapshot] to take a snapshot that allows to write to state objects
     *    inside of it.
     *
     * 2. Enter the snapshot and update the name to "Jessica Jones".
     *
     * 3. apply the snapshot changes right before the final assertion.
     *
     * 4. Run the test. Since we are taking the snapshot right after setting the name to "Aleesha
     *    Salgado", that will be the value of the name outside of it. It will only be "Jessica
     *    Jones" within the context of the snapshot (see assertions) and also outside but only after
     *    applying the changes (step 3).
     */
    @Test
    fun `write to state in the snapshot and propagate changes`() {
        var name by mutableStateOf("")
        name = "Aleesha Salgado"
        val snapshot = takeMutableSnapshot()

        snapshot.enter { name = "Jessica Jones" }

        assertThat(name, `is`("Aleesha Salgado"))
        snapshot.enter { assertThat(name, `is`("Jessica Jones")) }
        assertThat(name, `is`("Aleesha Salgado"))

        snapshot.apply() // propagate changes
        assertThat(name, `is`("Jessica Jones"))
    }

    /**
     * 1. Modify the name to be "Jessica Jones" within the first Snapshot, right before taking the
     *    second (nested) one.
     *
     * 2. Modify the name again to be "Cassandra Higgins" within the second Snapshot (nested one).
     *
     * 3. Apply the changes from the second right after its enter {} block.
     *
     * 4. Apply the changes of the first one right after its enter {} block.
     *
     * 5. Run the test, the global snapshot (root level) should be able to read "Cassandra Higgins"
     *    now since changes have been propagated all the way to the root.
     */
    @Test
    fun `propagate changes from nested snapshots`() {
        var name by mutableStateOf("")
        name = "Aleesha Salgado"

        val first = takeMutableSnapshot()
        first.enter {
            name = "Jessica Jones"

            val second = takeMutableSnapshot()
            second.enter { name = "Cassandra Higgins" }
            second.apply()
        }
        first.apply()
        assertThat(name, `is`("Cassandra Higgins"))
    }

    @Test
    fun `track reads and writes within a snapshot`() {
        var name by mutableStateOf("")
        name = "Aleesha Salgado"
        val readWriteCounter = ReadWriteCounter()
        val snapshot = takeMutableSnapshot(readWriteCounter)

        snapshot.enter { name = "Jessica Jones" }
        assertThat(readWriteCounter.reads(), `is`(0))
        assertThat(readWriteCounter.writes(), `is`(1))
    }

    private fun takeReadOnlySnapshot(): Snapshot = Snapshot.takeSnapshot()
    private fun takeMutableSnapshot(): MutableSnapshot = Snapshot.takeMutableSnapshot()

    private fun takeMutableSnapshot(readWriteCounter: ReadWriteCounter) =
        Snapshot.takeMutableSnapshot(
            readObserver = { readWriteCounter.trackRead() },
            writeObserver = { readWriteCounter.trackWrite() }
        )
}
