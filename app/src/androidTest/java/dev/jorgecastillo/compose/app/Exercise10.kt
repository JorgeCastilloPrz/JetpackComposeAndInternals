package dev.jorgecastillo.compose.app

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.printToLog
import dev.jorgecastillo.compose.app.data.FakeSpeakerRepository
import dev.jorgecastillo.compose.app.ui.composables.SpeakersScreen
import dev.jorgecastillo.compose.app.ui.theme.ComposeAndInternalsTheme
import org.junit.Rule
import org.junit.Test
import dev.jorgecastillo.compose.app.ui.composables.SpeakersRecompositionScreen
import dev.jorgecastillo.compose.app.viewmodel.SpeakersViewModel

/**
 * ### Exercise 10 👩🏾‍💻
 *
 * Let's improve exercise 3 (SpeakersScreen) in this one. I have created a copy of it in
 * [SpeakersRecompositionScreen]. This copy changes the Scaffold content by a SwipeToRefresh that
 * loads the list of speakers from a ViewModel. The [SpeakersViewModel] is a stub, so every time
 * it loads the list (or refreshes it), it returns the same exact list. The list is displayed using
 * [SpeakersRecompositionScreen], which is just the same content we had before for the screen.
 *
 * The goal of this exercise is to show how returning a List (kotlin stdlib) is considered unstable
 * by the Compose compiler, and it makes the Column and all its items recompose every time, even if
 * it is exactly the same list.
 *
 * To complete this exercise:
 *
 * 1. Replace the content within the theme in MainActivity by SpeakersRecompositionScreen(), and
 *    run the app. Open the LayoutInspector, and enable recomposition counts (in the eye icon). Do
 *    several pull to refreshes, and you'll see how all the elements within the column recompose
 *    every time.
 *
 * 2. Now, go to the [SpeakersViewModel] and you'll find a SpeakersState, which is the class we are
 *    using to represent our UI state. Every time the ViewModel emits, it emits an instance of this
 *    class. Create a new class to wrap the List<Speaker> we have in the [SpeakersState], and
 *    annotate it with @Immutable (Compose). Use this new class in the [SpeakersState], instead of
 *    the raw List<Speaker>. With this, we are aiding the Compiler and letting it know that the
 *    List we are using is truly immutable.
 *
 * 3. Go back to [SpeakersRecompositionScreen] and update the SpeakersRecompositionScreen(speakers: List<Speaker>) call
 *    to support the new Immutable wrapper class instead of the raw List<Speaker>. I.e:
 *
 *    SpeakersRecompositionScreen(speakers: ImmutableList<Speaker>) (or whatever you have called the wrapper)
 *
 * 4. Run the app again and perform several pull to refresh actions. Note how the column items are
 *    not recomposed anymore! The runtime can trust the input state now thanks to our help.
 *
 * 5. Run the test.
 */
class Exercise10Test {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun all_speakers_are_displayed() {
        // Start the app
        composeTestRule.setContent {
            ComposeAndInternalsTheme {
                SpeakersScreen(speakers = FakeSpeakerRepository().getSpeakers().take(7))
            }
        }

        composeTestRule.onNodeWithTag("SpeakersList").performScrollToNode(hasText("John Doe"))
        composeTestRule.onNodeWithText("John Doe").assertIsDisplayed()
        composeTestRule.onNodeWithText("Uber").assertIsDisplayed()

        composeTestRule.onRoot().printToLog("Exercise 3")

        composeTestRule.onNodeWithTag("SpeakersList").performScrollToNode(hasText("Sylvia Lotte"))
        composeTestRule.onNodeWithText("Sylvia Lotte").assertIsDisplayed()
        composeTestRule.onNodeWithText("Lyft").assertIsDisplayed()

        composeTestRule.onRoot().printToLog("Exercise 3")

        composeTestRule.onNodeWithTag("SpeakersList").performScrollToNode(hasText("Apis Anoubis"))
        composeTestRule.onNodeWithText("Apis Anoubis").assertIsDisplayed()
        composeTestRule.onNodeWithText("Twitter").assertIsDisplayed()

        composeTestRule.onRoot().printToLog("Exercise 3")

        composeTestRule.onNodeWithTag("SpeakersList").performScrollToNode(hasText("Aeolus Phrixos"))
        composeTestRule.onNodeWithText("Aeolus Phrixos").assertIsDisplayed()
        composeTestRule.onNodeWithText("Meta").assertIsDisplayed()

        composeTestRule.onRoot().printToLog("Exercise 3")

        composeTestRule.onNodeWithTag("SpeakersList").performScrollToNode(hasText("Oz David"))
        composeTestRule.onNodeWithText("Oz David").assertIsDisplayed()
        composeTestRule.onNodeWithText("Apple").assertIsDisplayed()

        composeTestRule.onRoot().printToLog("Exercise 3")

        composeTestRule.onNodeWithTag("SpeakersList")
            .performScrollToNode(hasText("Jagoda Viktorija"))
        composeTestRule.onNodeWithText("Jagoda Viktorija").assertIsDisplayed()
        composeTestRule.onNodeWithText("Google").assertIsDisplayed()

        composeTestRule.onRoot().printToLog("Exercise 3")

        composeTestRule.onNodeWithTag("SpeakersList").performScrollToNode(hasText("Dympna Bride"))
        composeTestRule.onNodeWithText("Dympna Bride").assertIsDisplayed()
        composeTestRule.onNodeWithText("Snapchat").assertIsDisplayed()

        composeTestRule.onRoot().printToLog("Exercise 3")
    }
}
