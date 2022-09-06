@file:Suppress("TestFunctionName")

package dev.jorgecastillo.compose.app

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import androidx.compose.ui.unit.dp
import dev.jorgecastillo.compose.app.data.FakeSpeakerRepository
import dev.jorgecastillo.compose.app.models.Speaker
import dev.jorgecastillo.compose.app.ui.composables.SpeakerCard
import dev.jorgecastillo.compose.app.ui.theme.ComposeAndInternalsTheme
import org.junit.Rule
import org.junit.Test

/**
 * ### Exercise 8 👩🏾‍💻
 *
 * In this exercise we must create a custom theme for an app that we will validate via screenshot
 * tests using Paparazzi to render our app without a physical device or emulator. That will let us
 * pick a fixed device configuration so tests work the same for all of us.
 *
 * Use the screenshot named theming.jpg in the screenshots directory from the project root as a
 * reference.
 *
 * 1.
 *
 * 2.
 *
 * 2. Run the test.
 */
class Exercise8Test {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun phone_ui_displayed_for_phones() {
        // Start the app
        composeTestRule.setContent {
            with(LocalDensity.current) {
                val widthDp = 1080.toDp()
                val heightDp = 2400.toDp()

                ComposeAndInternalsTheme {
                    // Emulate phone size with a Box
                    Box(Modifier.size(widthDp, heightDp)) {
                        AdaptativeScreen()
                    }
                }
            }
        }

        composeTestRule.onNodeWithText("John Doe").assertIsDisplayed()
        composeTestRule.onNodeWithText("Uber").assertIsDisplayed()

        composeTestRule.onRoot().printToLog("Exercise 7")
    }
}

@Composable
private fun AdaptativeScreen() {
    val speakers = FakeSpeakerRepository().getSpeakers()
    val speaker = speakers.first()
    val friends = speakers.drop(1)

    BoxWithConstraints(Modifier.fillMaxSize()) {
        val isTablet = maxWidth >= 600.dp
        if (isTablet) {
            Row {
                ProfileScreen(
                    speaker,
                    Modifier
                        .width(320.dp)
                        .fillMaxHeight()
                )
                FriendsScreen(friends, Modifier.weight(1f))
            }
        } else {
            ProfileScreen(speaker, Modifier.fillMaxSize())
        }
    }
}

@Composable
private fun ProfileScreen(speaker: Speaker, modifier: Modifier = Modifier) {
    Card(modifier.padding(dimensionResource(id = R.dimen.spacing_small))) {
        Row(Modifier.padding(dimensionResource(id = R.dimen.spacing_regular))) {
            Image(
                painter = painterResource(R.drawable.avatar_1),
                contentDescription = stringResource(
                    id = R.string.content_desc_speaker_avatar,
                    speaker.name
                ),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.avatar_size))
                    .clip(CircleShape)
            )
            Column(Modifier.padding(start = dimensionResource(id = R.dimen.spacing_regular))) {
                Text(text = speaker.name, style = MaterialTheme.typography.h6)
                Text(text = speaker.company, style = MaterialTheme.typography.caption)
            }
        }
    }
}

@Composable
private fun FriendsScreen(speakers: List<Speaker>, modifier: Modifier = Modifier) {
    LazyColumn(modifier.testTag("SpeakersList")) {
        items(speakers) { speaker ->
            SpeakerCard(speaker)
        }
    }
}
