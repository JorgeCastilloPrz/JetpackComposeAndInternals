package dev.jorgecastillo.compose.app

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.printToLog
import dev.jorgecastillo.compose.app.data.FakeSpeakerRepository
import dev.jorgecastillo.compose.app.ui.composables.LazySpeakersScreen
import dev.jorgecastillo.compose.app.ui.composables.SpeakerCard
import dev.jorgecastillo.compose.app.ui.composables.SpeakersScreen
import dev.jorgecastillo.compose.app.ui.theme.ComposeAndInternalsTheme
import org.junit.Rule
import org.junit.Test

/**
 * ### Exercise 4 👩🏾‍💻
 *
 * This exercise goes on top of Exercise 3. The screen to implement is [LazySpeakersScreen].
 *
 * Please use the same exercise3.png image from the screenshots directory at the root of this
 * project as a reference for this exercise. It should look exactly the same than exercise 3.
 *
 * To complete this exercise:
 *
 * 1. Feel free to copy the body of the [SpeakersScreen] Composable from exercise 3 into the
 *   [LazySpeakersScreen] one. Do not copy anything else. This Composable will reuse the
 *   [SpeakerCard] from the previous exercise as is.
 *
 * 2. Replace the Column used to list the Speakers by a [LazyColumn]. Use the [items] function with
 *    the speakers collection to provide the Composable you want to show for each one of the
 *    speakers (SpeakerCard). You should be able to remove the verticalScroll modifier now. DO NOT
 *    remove the testTag, that is still needed for the test validation.
 *
 * 3. Run the test.
 */
class Exercise4Test {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun all_speakers_are_displayed() {
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
