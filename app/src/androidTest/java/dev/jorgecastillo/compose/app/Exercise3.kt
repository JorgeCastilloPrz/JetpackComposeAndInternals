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

/**
 * ### Exercise 3 👩🏾‍💻
 *
 * This exercise is a bit longer than the previous ones. We want to create a complete screen where
 * we will show a scrollable list of Speakers. The screen to implement is [SpeakersScreen].
 *
 * Please use the exercise3.png image from the screenshots directory at the root of this project as
 * a reference for this exercise.
 *
 * There is a preview that you can use for all this work to double check it looks the way it
 * needs. On top of that, you can check the provided screenshot, or even set the SpeakersScreen
 * Composable as the content of the MainActivity in order to run the app in an emulator like:
 *
 *     override fun onCreate(savedInstanceState: Bundle?) {
 *       super.onCreate(savedInstanceState)
 *       setContent {
 *         val speakersRepository = FakeSpeakerRepository()
 *         ComposeAndInternalsTheme {
 *           SpeakersScreen(speakers = speakersRepository.getSpeakers())
 *         }
 *       }
 *      }
 *
 *    The fake repository will provide the speakers we need.
 *
 * To complete this exercise:
 *
 * 1. Use Scaffold at the root level. The Scaffold Composable gives you the material looks of an
 *    average Android app, with parameters to add a TopAppBar, a FAB, a BottomBar, some content for
 *    the screen and a few more things. It's pretty much like a template Composable to build an
 *    average Android UI.
 *
 * 2. Add a TopAppBar to the Scaffold with the title "Speakers". You can use a normal Text
 *    Composable for the title text.
 *
 * 3. Add a FloatingActionButton with an "add" material icon. You can use the Icon Composable for
 *    this, and rememberVectorPainter to draw the icon on it. You can use Icons.Default.Add as the
 *    icon vector. Feel free to leave the onClick lambda empty for now.
 *
 * 4. For the content lambda, use a Column where we will show the list of speakers. Remember to use
 *    the provided PaddingValues argument from the content lambda to add it as padding
 *    (i.e: Modifier.padding) to the Column at the root of the content. This is needed to ensure
 *    that the correct offset is used to leave room for the potential TopAppBar and BottomAppBar.
 *
 * 5. Make the Column scrollable by applying a Modifier.verticalScroll(rememberScrollState()) to it.
 *    We will understand states later in this course, so bear with me for now. You can chain this
 *    new modifier with the previous one (padding) by using the dot notation:
 *
 *    Modifier
 *     .padding(contentPadding)
 *     .verticalScroll(rememberScrollState())
 *
 *    This is true for all modifiers, and I recommend you to do it from now on wherever you have
 *    more than one modifier in a Composable.
 *
 *    Add a testTag to it called "SpeakersList" like Modifier.testTag("SpeakersList"). This is
 *    really important, since the test needs this to find the list and interact with it.
 *
 * 6. Let's add some logic to our Composable now. Since Composables are plain Kotlin functions, we
 *    can use any Kotlin goodies inside, like any of the collection apis. forEach {} speaker on the
 *    list provided as a parameter, add a SpeakerCard to the column to draw the Speaker.
 *
 *    Time to implement the SpeakerCard.
 *
 * 7. Use the Card Composable for the SpeakerCard, and make it fill the max available width by
 *    adding the Modifier.fillMaxWidth() to it. You can also add some padding to it like
 *    R.dimen.spacing_small. To load resources in Compose, there are built in utility functions
 *    you can use, like dimensionResource() in this case.
 *
 * 8. Inside the Card, let's add a Row with some padding like R.dimen.spacing_regular. This Row will
 *    contain an Image Composable for the speaker avatar, and a Column with two texts for the name
 *    and the company. To draw the image you'll need to pass a painterResource, and you can use the
 *    provided function avatarResForId(speaker.id) in order to get the corresponding image resource.
 *    These images are local drawables. Make the image contentScale be ContentScale.Crop so it fills
 *    the Composable size completely without stretching. Set a size of
 *    Modifier.size(dimensionResource(id = R.dimen.avatar_size)) to the Image and clip it as a
 *    circle with Modifier.clip(CircleShape). (Note that there are built in shapes for different
 *    shapes apart from Circle. Add the mentioned Column after the Image, and set some
 *    padding to it, e.g: R.dimen.spacing_regular.
 *
 * 9. Finally, add two Text Composables inside the Column with the material styles h6 and caption.
 *    You can do it via the style parameter like: MaterialTheme.typography.h6. This will grab the
 *    text style from the material theme we have already applied to the app. Show the texts
 *    speaker.name and speaker.company on them. We will learn material and theming in Compose
 *    in-depth later in this course.
 *
 * 10. Run the test.
 */
class Exercise3Test {

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
