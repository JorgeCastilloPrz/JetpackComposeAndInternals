package dev.jorgecastillo.compose.app

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.printToLog
import androidx.navigation.compose.composable
import dev.jorgecastillo.compose.app.ui.composables.MainScreen
import dev.jorgecastillo.compose.app.ui.composables.SpeakerFeed
import dev.jorgecastillo.compose.app.ui.composables.SpeakerProfileScreen
import dev.jorgecastillo.compose.app.ui.theme.ComposeAndInternalsTheme
import org.junit.Rule
import org.junit.Test

/**
 * ### Exercise 12 👩🏾‍💻
 *
 * This exercise adds Compose Navigation to our app. The screen to implement is [MainScreen].
 *
 * Please use exercise12.gif, exercise11a.png, and exercise11b.png from the screenshots directory at
 * the root of this project as a references for this exercise.
 *
 * To complete this exercise:
 *
 * 1. Add a Scaffold to the [MainScreen] with a TopAppBar with a title (see images).
 *
 * 2. In the content lambda, remember a navigation controller to hoist the navigation state
 *    (rememberNavController()).
 *
 * 3. Add a NavHost to the content lambda, set the navigation controller to it, and set the start
 *    destination to be "speakers".
 *
 * 4. Add a couple composable routes to the NavHost via the [composable] function. One of them will
 *    have the route "speakers", the other one will be "speaker/{speakerId}".
 *
 * 5. Make the NavHost show the [SpeakerFeed] in the "speakers" route, and set a callback so it
 *    navigates to speaker/{speakerId} onClick using the nav controller (navController.navigate()).
 *    You'll need to forward the clicked speaker id.
 *
 * 6. In the "speaker/{speakerId}" route, show the [SpeakerProfileScreen] and pass the speaker id to
 *    it. You can use the backStackEntry.arguments?.getString("speakerId") to retrieve it.
 *
 * 7. Run the test.
 */
class Exercise12 {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun feed_shows_first_and_clicking_a_speaker_shows_profile() {
        // Start the app
        composeTestRule.setContent {
            ComposeAndInternalsTheme {
                MainScreen()
            }
        }

        composeTestRule.onNodeWithTag("SpeakersList").performScrollToNode(hasText("John Doe"))
        composeTestRule.onNodeWithText("John Doe").assertIsDisplayed()
        composeTestRule.onNodeWithText("Uber").assertIsDisplayed()

        composeTestRule.onRoot().printToLog("Exercise 11")

        composeTestRule.onNodeWithTag("SpeakersList").performScrollToNode(hasText("Sylvia Lotte"))
        composeTestRule.onNodeWithText("Sylvia Lotte").assertIsDisplayed()
        composeTestRule.onNodeWithText("Lyft").assertIsDisplayed()

        composeTestRule.onRoot().printToLog("Exercise 11")

        composeTestRule.onNodeWithTag("SpeakersList").performScrollToNode(hasText("Apis Anoubis"))
        composeTestRule.onNodeWithText("Apis Anoubis").assertIsDisplayed()
        composeTestRule.onNodeWithText("Twitter").assertIsDisplayed()

        composeTestRule.onRoot().printToLog("Exercise 11")

        composeTestRule.onNodeWithText("Apis Anoubis").performClick()

        composeTestRule.onNodeWithText("Follow").assertIsDisplayed()
    }
}
