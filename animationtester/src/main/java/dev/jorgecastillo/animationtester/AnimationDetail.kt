@file:Suppress("DEPRECATION")

package dev.jorgecastillo.animationtester

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.jorgecastillo.animationtester.detail.BetweenComposables
import dev.jorgecastillo.animationtester.detail.LazyListItemChanges
import dev.jorgecastillo.animationtester.detail.MultipleProperties
import dev.jorgecastillo.animationtester.detail.Properties
import dev.jorgecastillo.animationtester.detail.RepeatAnimation
import dev.jorgecastillo.animationtester.detail.SizeChanges
import dev.jorgecastillo.animationtester.detail.StartOnLaunch
import dev.jorgecastillo.animationtester.detail.Visibility
import dev.jorgecastillo.animationtester.detail.VisibilityCustom
import dev.jorgecastillo.animationtester.detail.VisibilityViaProperties
import dev.jorgecastillo.animationtester.ui.theme.JetpackComposeAndInternalsTheme

class AnimationDetail : ComponentActivity() {
    companion object {
        const val EXTRAS = "ANIMATION_DETAIL_EXTRAS"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val option: Option? = intent.getParcelableExtra(EXTRAS)

        setContent {
            JetpackComposeAndInternalsTheme {
                Scaffold(
                    topBar = { TopAppBar(title = { Text(option?.name ?: "Detail") }) },
                    content = { contentPadding ->
                        Box(Modifier.padding(contentPadding)) {
                            if (option != null) {
                                ContentForOption(option)
                            } else {
                                Text("Please provide an option when navigating to this Activity.")
                            }
                        }
                    }
                )
            }
        }
    }

    @Composable
    private fun ContentForOption(option: Option): Unit {
        when (option) {
            is Option.BetweenComposables -> BetweenComposables()
            is Option.LazyListItemChanges -> LazyListItemChanges()
            is Option.MultipleProperties -> MultipleProperties()
            is Option.Properties -> Properties()
            is Option.RepeatAnimation -> RepeatAnimation()
            is Option.Sequential -> Visibility()
            is Option.SizeChanges -> SizeChanges()
            is Option.StartOnLaunch -> StartOnLaunch()
            is Option.Visibility -> Visibility()
            is Option.VisibilityCustom -> VisibilityCustom()
            is Option.VisibilityViaProperties -> VisibilityViaProperties()
        }
    }
}
