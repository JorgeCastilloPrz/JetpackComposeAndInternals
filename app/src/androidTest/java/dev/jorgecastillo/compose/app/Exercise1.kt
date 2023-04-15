@file:Suppress("TestFunctionName")

package dev.jorgecastillo.compose.app

import androidx.compose.foundation.layout.Box
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import dev.jorgecastillo.compose.app.ui.theme.ComposeAndInternalsTheme
import dev.jorgecastillo.compose.app.ui.composables.NamePlate
import org.junit.Rule
import org.junit.Test

/**
 * ### Exercise 1 👩🏾‍💻
 *
 * This UI test runs the [NamePlate] Composable within an empty Activity and asserts over the value
 * of the displayed name. It does not assert over its looks so we are not checking if it is centered
 * or not yet. Keep that in mind while running it.
 *
 * To complete this exercise:
 *
 * 1. Use a [Box] that fills the complete screen (width and height). (Check Modifier.fillMaxSize()).
 *    We will learn modifiers later in this course, but for now we can think of them as the View
 *    attributes we are familiar with. They allow to tweak how Composables look and behave.
 * 2. Fit a Text inside with the provided text. Center it within the Box using the Box configuration
 *    options.
 * 3. Run the test.
 */
class Exercise1Test {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun text_displayed_in_the_box() {
        composeTestRule.setContent {
            ComposeAndInternalsTheme {
                NamePlate("John Doe")
            }
        }

        composeTestRule.onNodeWithText("John Doe").assertIsDisplayed()
    }
}
