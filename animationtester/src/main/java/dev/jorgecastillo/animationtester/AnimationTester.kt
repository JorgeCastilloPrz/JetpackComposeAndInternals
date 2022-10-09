package dev.jorgecastillo.animationtester

import android.os.Parcelable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.parcelize.Parcelize

sealed class Option : Parcelable {
    abstract val name: String

    @Parcelize
    data class Visibility(override val name: String = "Visibility") : Option()

    @Parcelize
    data class VisibilityCustom(override val name: String = "Visibility custom") : Option()

    @Parcelize
    data class VisibilityViaProperties(override val name: String = "Visibility via properties") :
        Option()

    @Parcelize
    data class SizeChanges(override val name: String = "SizeChanges") : Option()

    @Parcelize
    data class Properties(override val name: String = "Animating properties") : Option()

    @Parcelize
    data class LazyListItemChanges(override val name: String = "Lazy list item changes") : Option()

    @Parcelize
    data class BetweenComposables(override val name: String = "Changes between Composables") :
        Option()

    @Parcelize
    data class MultipleProperties(override val name: String = "Multiple properties") : Option()

    @Parcelize
    data class RepeatAnimation(override val name: String = "Repeat animation") : Option()

    @Parcelize
    data class StartOnLaunch(override val name: String = "Start animation on launch") : Option()

    @Parcelize
    data class Sequential(override val name: String = "SequentialAnimations") : Option()
}

@Composable
fun AnimationTester(onClick: (Option) -> Unit = {}) {
    LazyColumn {
        item { AnimationOption(Option.Visibility(), onClick) }
        item { Divider() }
        item { AnimationOption(Option.VisibilityCustom(), onClick) }
        item { Divider() }
        item { AnimationOption(Option.VisibilityViaProperties(), onClick) }
        item { Divider() }
        item { AnimationOption(Option.SizeChanges(), onClick) }
        item { Divider() }
        item { AnimationOption(Option.Properties(), onClick) }
        item { Divider() }
        item { AnimationOption(Option.LazyListItemChanges(), onClick) }
        item { Divider() }
        item { AnimationOption(Option.BetweenComposables(), onClick) }
        item { Divider() }
        item { AnimationOption(Option.MultipleProperties(), onClick) }
        item { Divider() }
        item { AnimationOption(Option.RepeatAnimation(), onClick) }
        item { Divider() }
        item { AnimationOption(Option.StartOnLaunch(), onClick) }
        item { Divider() }
        item { AnimationOption(Option.Sequential(), onClick) }
    }
}

@Composable
private fun AnimationOption(option: Option, onClick: (Option) -> Unit) {
    Text(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick(option) }
        .padding(16.dp), text = option.name)
}
