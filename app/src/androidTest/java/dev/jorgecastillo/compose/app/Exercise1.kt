package dev.jorgecastillo.compose.app

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import dev.jorgecastillo.compose.app.ui.theme.ComposeAndInternalsTheme
import org.junit.Rule
import org.junit.Test

/**
 * ### Exercise 1 👩🏾‍💻
 *
 * This UI test runs the NamePlate Composable within an empty Activity and asserts over the value of
 * the displayed name, and its alignment within the parent.
 *
 * To complete this exercise:
 *
 * 1. Use a Box that fills the complete screen (width and height). To achieve that you can pass a
 *    modifier = Modifier.fillMaxSize() to it. We will learn modifiers later in this course, but for
 *    now we can think of them as the View attributes we are familiar with. They allow to tweak how
 *    Composables look and behave.
 * 2. Fit a Text inside with the provided text. Center it within the Box using the Box configuration
 *    options.
 * 3. Run the test.
 */
class Exercise1Test {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun text_displayed_and_centered_within_the_box() {
        // Start the app
        composeTestRule.setContent {
            ComposeAndInternalsTheme {
                NamePlate("John Doe")
            }
        }

        // Assert for the names from the repo in order: "Jane Smith", "Aleesha Salgado",
        // "Alayna Bradley", "Zunaira English", "Cassandra Higgins", then starts again from the
        // beginning.
        composeTestRule.onNodeWithText("John Doe").assertIsDisplayed()
        composeTestRule.onRoot().printToLog("Exercise 1")
    }
}
