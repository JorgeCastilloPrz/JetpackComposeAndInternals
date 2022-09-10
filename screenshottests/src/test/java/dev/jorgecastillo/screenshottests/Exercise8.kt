@file:Suppress("TestFunctionName")

package dev.jorgecastillo.compose.app

import app.cash.paparazzi.DeviceConfig.Companion.PIXEL_5
import app.cash.paparazzi.Paparazzi
import app.cash.paparazzi.androidHome
import app.cash.paparazzi.detectEnvironment
import dev.jorgecastillo.compose.app.ui.theme.ComposeAndInternalsTheme
import dev.jorgecastillo.screenshottests.theme.FakeSpeakerRepository
import dev.jorgecastillo.screenshottests.theme.LazySpeakersScreen
import dev.jorgecastillo.screenshottests.theme.SpeakerCard
import org.junit.Rule
import org.junit.Test

/**
 * ### Exercise 8 👩🏾‍💻
 *
 * This exercise is validated via screenshot tests using Paparazzi to render our app without a
 * physical device or emulator. That will let us pick a fixed device configuration so tests work the
 * same for all of us.
 *
 * Paparazzi does not support app modules yet, that is why this test is on a library module.
 *
 * Use the screenshot named exercise8.png in the screenshots directory from the project root as a
 * reference.
 *
 * 1. Add the following color definitions to the app:
 *    val Pink200 = Color(0xFFF48FB1)
 *    val Pink500 = Color(0xFFE91E63)
 *    val Pink700 = Color(0xFFC2185B)
 *
 * 2. Create a PinkTheme along with the [ComposeAndInternalsTheme] that uses the following color
 *    palettes but the same typography and shapes than the former:
 *
 *    private val DarkPinkColorPalette = darkColors(
 *      primary = Pink200,
 *      primaryVariant = Pink700,
 *      secondary = Teal200,
 *      surface = Pink200
 *    )
 *
 *    private val LightPinkColorPalette = lightColors(
 *      primary = Pink500,
 *      primaryVariant = Pink700,
 *      secondary = Teal200,
 *      surface = Pink700
 *    )
 *
 * 3. Before displaying a [SpeakerCard] for the [LazySpeakersScreen], check the speaker company. If
 *    it is a Lyft worker, use the PinkTheme. Otherwise keep the default theme.
 *
 * 4. Run the test.
 */
class Exercise8Test {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = PIXEL_5,
        theme = "android:Theme.Material.Light.NoActionBar",
        environment = detectEnvironment().copy(
            platformDir = "${androidHome()}/platforms/android-32",
            compileSdkVersion = 32
        ),
        // ...see docs for more options
    )

    @Test
    fun pink_theme_applied_as_expected() {
        paparazzi.snapshot {
            ComposeAndInternalsTheme {
                LazySpeakersScreen(speakers = FakeSpeakerRepository().getSpeakers().take(4))
            }
        }
    }
}
