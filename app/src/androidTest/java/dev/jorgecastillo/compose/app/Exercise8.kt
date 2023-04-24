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
import androidx.compose.runtime.ProvidableCompositionLocal
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
 *    scenarios than the alternative below.
 *
 * 2. [staticCompositionLocalOf]:
 *
 *    Reads to this object are not tracked by Compose, so when updated, it invalidates the entire
 *    `content` lambda where the CompositionLocal is provided. Use this one for cases where the
 *    value provided will likely not change ever, since it will be more performant for those.
 *
 * Now, time for the exercise.
 *
 * There are two tests on this class. For each one of these tests, we must provide a
 * CompositionLocal. That is [localTest1] and [localTest2]. (They are defined at a file level to
 * avoid compose lambdas to capture them, since that would invalidate those lambdas on every
 * recomposition and therefore defeat the purpose of these tests).
 *
 *
 * To complete this exercise:
 *
 * 1. For the first test, initialize [localTest1] as a non-static [CompositionLocal].
 * 2. Read from its value when setting the text to the nested [Text] Composable inside [MyRow].
 * 3. Run the test. It will click the button to increase the counter state 3 times. That will cause
 *    3 recompositions on top of the initial one. For any components within the
 *    CompositionLocalProvider content lambda, only the ones reading from the CompositionLocal will
 *    recompose. The test verifies this by checking how many times the other components recompose.
 *    Those should only recompose once. The test should pass.
 *
 * 4. For the second test, initialize [localTest2] as a **static** [CompositionLocal] this time.
 * 5. Read from its value when setting the text to the nested [Text] Composable inside [MyRow].
 * 6. Run the test. It will click the button to increase the counter state 3 times. That will cause
 *    3 recompositions on top of the initial one. This time, all components within the
 *    CompositionLocalProvider content lambda will recompose. The test verifies this and should
 *    pass.
 */
class Exercise8 {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        firstRecompositionCounter.reset()
        secondRecompositionCounter.reset()
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
        }
    }
}

private val localTest1: ProvidableCompositionLocal<Int> = compositionLocalOf { -1 }
private val localTest2: ProvidableCompositionLocal<Int> = staticCompositionLocalOf { -1 }

val firstRecompositionCounter = RecompositionCounter()
val secondRecompositionCounter = RecompositionCounter()

@Composable
private fun MyRow(content: @Composable () -> Unit) {
    Row {
        SideEffect { secondRecompositionCounter.increment() }
        content()
    }
}
