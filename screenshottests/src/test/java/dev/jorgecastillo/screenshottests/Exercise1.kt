@file:Suppress("TestFunctionName")

package dev.jorgecastillo.screenshottests

import androidx.compose.foundation.layout.Box
import app.cash.paparazzi.DeviceConfig.Companion.PIXEL_5
import app.cash.paparazzi.Paparazzi
import app.cash.paparazzi.androidHome
import app.cash.paparazzi.detectEnvironment
import dev.jorgecastillo.compose.app.ui.theme.ComposeAndInternalsTheme
import dev.jorgecastillo.screenshottests.theme.FakeSpeakerRepository
import dev.jorgecastillo.screenshottests.theme.LazySpeakersScreen
import dev.jorgecastillo.screenshottests.theme.NamePlate
import dev.jorgecastillo.screenshottests.theme.SpeakerCard
import org.junit.Rule
import org.junit.Test

/**
 * ### Exercise 1 👩🏾‍💻
 *
 * This exercise is validated via screenshot tests using Paparazzi to render our app without a
 * physical device or emulator. That will let us pick a fixed device configuration so tests work the
 * same for all of us.
 *
 * Paparazzi does not support app modules yet, that is why this test is on a library module.
 *
 * This UI test runs the [NamePlate] Composable within an empty Activity and asserts over the value of
 * the displayed name, and its alignment within the parent.
 *
 * To complete this exercise:
 *
 * 1. Use a [Box] that fills the complete screen (width and height). To achieve that you can pass a
 *    modifier = Modifier.fillMaxSize() to it. We will learn modifiers later in this course, but for
 *    now we can think of them as the View attributes we are familiar with. They allow to tweak how
 *    Composables look and behave.
 * 2. Fit a Text inside with the provided text. Center it within the Box using the Box configuration
 *    options.
 * 3. Run the test.
 */
class Exercise1Test {

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
    fun text_displayed_and_centered_within_the_box() {
        paparazzi.snapshot {
            ComposeAndInternalsTheme {
                NamePlate("John Doe")
            }
        }
    }
}
