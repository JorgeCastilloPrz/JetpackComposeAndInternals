@file:OptIn(ExperimentalMaterialApi::class)
@file:Suppress("TestFunctionName", "LocalVariableName", "PrivatePropertyName")

package dev.jorgecastillo.compose.app

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import dev.jorgecastillo.compose.app.ui.theme.ComposeAndInternalsTheme
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
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

    @Before
    fun setup() {
        firstRecompositionCounter.reset()
        secondRecompositionCounter.reset()
        thirdRecompositionCounter.reset()
    }

    @Test
    fun composition_local_skips_intermediate_composable_calls_not_reading_from_it() {
        composeTestRule.setContent {
            ComposeAndInternalsTheme {
                var value by remember { mutableStateOf(1) }

                Column {
                    CompositionLocalProvider(localTest1 provides value) {
                        SideEffect { firstRecompositionCounter.increment() }

                        MyRow {
                            Text("Text is ${localTest1.current}")
                        }
                    }

                    Button(
                        modifier = Modifier.testTag("button"),
                        onClick = { value++ }
                    ) {
                        SideEffect { secondRecompositionCounter.increment() }
                        Text("Increment")
                    }
                }
            }
        }

        composeTestRule.onNodeWithTag("button").performClick()
        composeTestRule.onNodeWithTag("button").performClick()
        composeTestRule.onNodeWithTag("button").performClick()
        composeTestRule.runOnIdle {
            assertThat(firstRecompositionCounter.count(), `is`(1))
            assertThat(secondRecompositionCounter.count(), `is`(1))
            assertThat(thirdRecompositionCounter.count(), `is`(1))
        }
    }

    @Test
    fun static_composition_local_recomposes_full_provider_content_lambda() {
        composeTestRule.setContent {
            ComposeAndInternalsTheme {
                var value by remember { mutableStateOf(1) }

                Column {
                    CompositionLocalProvider(localTest2 provides value) {
                        SideEffect { firstRecompositionCounter.increment() }

                        MyRow {
                            Text("Text is ${localTest2.current}")
                        }
                    }

                    Button(
                        modifier = Modifier.testTag("button"),
                        onClick = { value++ }
                    ) {
                        SideEffect { secondRecompositionCounter.increment() }
                        Text("Increment")
                    }
                }
            }
        }

        composeTestRule.onNodeWithTag("button").performClick()
        composeTestRule.onNodeWithTag("button").performClick()
        composeTestRule.onNodeWithTag("button").performClick()
        composeTestRule.runOnIdle {
            assertThat(firstRecompositionCounter.count(), `is`(4))
            assertThat(secondRecompositionCounter.count(), `is`(4))
            assertThat(thirdRecompositionCounter.count(), `is`(4))
        }
    }
}

private val localTest1 = compositionLocalOf { -1 }
private val localTest2 = staticCompositionLocalOf { -1 }

val firstRecompositionCounter = RecompositionCounter()
val secondRecompositionCounter = RecompositionCounter()
val thirdRecompositionCounter = RecompositionCounter()

@Composable
private fun MyRow(content: @Composable () -> Unit) {
    Row {
        SideEffect { thirdRecompositionCounter.increment() }
        content()
    }
}
