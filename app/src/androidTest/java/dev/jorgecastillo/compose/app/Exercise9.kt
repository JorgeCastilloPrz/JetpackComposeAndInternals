package dev.jorgecastillo.compose.app

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import dev.jorgecastillo.compose.app.data.FakeSpeakerRepository
import org.junit.Rule
import org.junit.Test

/**
 * ### Exercise 9 👩🏾‍💻
 *
 * This exercise is not validated, so you should validate your implementation by comparing it with
 * the provided screenshots/exercise9.png picture.
 *
 * 1. Add the following color definitions to the Color.kt file in this same package:
 *
 *    val Pink200 = Color(0xFFF48FB1)
 *    val Pink500 = Color(0xFFE91E63)
 *    val Pink700 = Color(0xFFC2185B)
 *
 * 2. Create a PinkTheme along with the [MyAppTheme] that uses the following color
 *    palettes but the same typography and shapes than the former:
 *
 *    private val DarkPinkColors = darkColors(
 *      primary = Pink200,
 *      primaryVariant = Pink700,
 *      secondary = Teal200,
 *      surface = Pink200
 *    )
 *
 *    private val LightPinkColors = lightColors(
 *      primary = Pink500,
 *      primaryVariant = Pink700,
 *      secondary = Teal200,
 *      surface = Pink700
 *    )
 *
 * 3. Before displaying a [SpeakerCard] for the [Feed], check the speaker company. If it is a Lyft
 *    worker and theme it with the PinkTheme in that case. Otherwise keep the default [MyAppTheme].
 *
 * 4. Run the test.
 */
class Exercise9 {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun test_nested_themes() {
        composeTestRule.setContent {
            MyAppTheme {
                Feed(speakers = FakeSpeakerRepository().getSpeakers().take(5))
            }
        }

        composeTestRule.onRoot().printToLog("Exercise 9")
    }
}
