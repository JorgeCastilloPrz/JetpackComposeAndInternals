package dev.jorgecastillo.compose.app

import androidx.compose.material.Card
import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.printToLog
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.jorgecastillo.compose.app.data.FakeSpeakerRepository
import dev.jorgecastillo.compose.app.ui.composables.LazySpeakersScreen
import dev.jorgecastillo.compose.app.ui.theme.ComposeAndInternalsTheme
import org.junit.Rule
import org.junit.Test

/**
 * ### Exercise 8 👩🏾‍💻
 *
 * This exercise introduces [CompositionLocal], the two different ways to create a CompositionLocal,
 * and the difference between the two.
 *
 * There are two ways to create a CompositionLocal:
 *
 * 1. [compositionLocalOf]:
 *
 *    When updated, invalidates only the Composables reading its current value. Use it for cases
 *    where the value is expected to change over time, since it is more performant in those
 *    scenarios than the alternative below 👇
 *
 * 2. [staticCompositionLocalOf]:
 *
 *    Reads to this object are not tracked by Compose, so when updated, it invalidates the entire
 *    `content` lambda where the CompositionLocal is provided. Use this one for cases where the
 *    value provided will likely not change ever, since it will be more performant for those.
 *
 * Now, time for the exercise.
 *
 * Imagine that we have a custom design system where we need to provide some custom elevation value
 * for all the elevated components like [Card]s.
 *
 * There are two tests on this test class. In both of them, we must provide the elevation via a
 * [CompositionLocal] so it is exposed to the whole subtree, and then read from its current value to
 * set the elevation of a nested [Card] Composable. The elevation will be defined via the
 * [Elevations] data class.
 *
 * To complete this exercise:
 *
 * 1. Provide the elevation using a static [CompositionLocal] in the first test. Read from it to set
 *    the [Card] elevation.
 * 2. Run the test, it should verify that all Composables are recomposed once the value is updated.
 *
 * 3. Provide the elevation using a non-static [CompositionLocal] in the second test. Read from it
 *    to set the [Card] elevation.
 * 4. Run the test, it should verify that only the Composables reading from the value are
 *    recomposed.
 */
class Exercise8 {

    @get:Rule
    val composeTestRule = createComposeRule()

    data class Elevations(val card: Dp = 0.dp, val default: Dp = 0.dp)

    @Test
    fun static_composition_local_does_not_trigger_granular_recomposition() {
        // Start the app
        composeTestRule.setContent {
            ComposeAndInternalsTheme {
                LazySpeakersScreen(speakers = FakeSpeakerRepository().getSpeakers().take(7))
            }
        }

        composeTestRule.onNodeWithTag("SpeakersList").performScrollToNode(hasText("John Doe"))
        composeTestRule.onNodeWithText("John Doe").assertIsDisplayed()
        composeTestRule.onNodeWithText("Uber").assertIsDisplayed()

        composeTestRule.onRoot().printToLog("Exercise 4")

        composeTestRule.onNodeWithTag("SpeakersList").performScrollToNode(hasText("Sylvia Lotte"))
        composeTestRule.onNodeWithText("Sylvia Lotte").assertIsDisplayed()
        composeTestRule.onNodeWithText("Lyft").assertIsDisplayed()

        composeTestRule.onRoot().printToLog("Exercise 4")

        composeTestRule.onNodeWithTag("SpeakersList").performScrollToNode(hasText("Apis Anoubis"))
        composeTestRule.onNodeWithText("Apis Anoubis").assertIsDisplayed()
        composeTestRule.onNodeWithText("Twitter").assertIsDisplayed()

        composeTestRule.onRoot().printToLog("Exercise 4")

        composeTestRule.onNodeWithTag("SpeakersList").performScrollToNode(hasText("Aeolus Phrixos"))
        composeTestRule.onNodeWithText("Aeolus Phrixos").assertIsDisplayed()
        composeTestRule.onNodeWithText("Meta").assertIsDisplayed()

        composeTestRule.onRoot().printToLog("Exercise 4")

        composeTestRule.onNodeWithTag("SpeakersList").performScrollToNode(hasText("Oz David"))
        composeTestRule.onNodeWithText("Oz David").assertIsDisplayed()
        composeTestRule.onNodeWithText("Apple").assertIsDisplayed()

        composeTestRule.onRoot().printToLog("Exercise 4")

        composeTestRule.onNodeWithTag("SpeakersList")
            .performScrollToNode(hasText("Jagoda Viktorija"))
        composeTestRule.onNodeWithText("Jagoda Viktorija").assertIsDisplayed()
        composeTestRule.onNodeWithText("Google").assertIsDisplayed()

        composeTestRule.onRoot().printToLog("Exercise 4")

        composeTestRule.onNodeWithTag("SpeakersList").performScrollToNode(hasText("Dympna Bride"))
        composeTestRule.onNodeWithText("Dympna Bride").assertIsDisplayed()
        composeTestRule.onNodeWithText("Snapchat").assertIsDisplayed()

        composeTestRule.onRoot().printToLog("Exercise 4")
    }
}