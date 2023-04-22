@file:OptIn(ExperimentalMaterialApi::class)
@file:Suppress("TestFunctionName", "LocalVariableName", "PrivatePropertyName")

package dev.jorgecastillo.compose.app

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.jorgecastillo.compose.app.ui.theme.ComposeAndInternalsTheme
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

    private lateinit var LocalCardElevation: ProvidableCompositionLocal<Dp>
    private val repo: FakeFollowerRepository = FakeFollowerRepository()

    @Test
    fun static_composition_local_does_not_trigger_granular_recomposition() {
        composeTestRule.setContent {
            ComposeAndInternalsTheme {
                LocalCardElevation = compositionLocalOf { 0.dp }
                var cardElevation by remember { mutableStateOf(0.dp) }

                CompositionLocalProvider(LocalCardElevation provides cardElevation) {
                    LazyColumn(Modifier.padding(16.dp)) {
                        item {
                            Switch(
                                modifier = Modifier.testTag("toggle"),
                                checked = cardElevation != 0.dp,
                                onCheckedChange = {
                                    cardElevation = if (cardElevation == 0.dp) 8.dp else 0.dp
                                }
                            )
                        }

                        items(repo.getFollowers()) { follower ->
                            FollowerCard(follower)
                        }
                    }
                }
            }
        }

        composeTestRule.onNodeWithTag("toggle").performClick()
        composeTestRule.onNodeWithTag("toggle").performClick()
        composeTestRule.onNodeWithTag("toggle").performClick()
    }

    @Composable
    private fun FollowerCard(follower: Follower, onClick: (Follower) -> Unit = {}) {
        Card(
            elevation = LocalCardElevation.current,
            onClick = { onClick(follower) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.spacing_small))
        ) {
            Column(Modifier.padding(dimensionResource(id = R.dimen.spacing_regular))) {
                Text(text = follower.name, style = MaterialTheme.typography.h6)
                Text(text = follower.country, style = MaterialTheme.typography.caption)
            }
        }
    }
}

data class Follower(val name: String, val country: String)
