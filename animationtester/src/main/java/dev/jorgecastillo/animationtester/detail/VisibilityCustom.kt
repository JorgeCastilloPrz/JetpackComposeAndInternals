@file:OptIn(ExperimentalMaterialApi::class)

package dev.jorgecastillo.animationtester.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import dev.jorgecastillo.animationtester.FakeSpeakerRepository
import dev.jorgecastillo.animationtester.SpeakerCard

@Composable
fun VisibilityCustom() {
    var editable by remember { mutableStateOf(true) }
    val density = LocalDensity.current

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.size(24.dp))

        Button(onClick = { editable = !editable }) {
            Text("Toggle visibility")
        }

        AnimatedVisibility(
            visible = editable,
            enter = slideInHorizontally { with(density) { -40.dp.roundToPx() } } +
                fadeIn(initialAlpha = 0.3f),
            exit = slideOutHorizontally() + fadeOut()
        ) {
            SpeakerCard(speaker = FakeSpeakerRepository().getSpeakers()[10])
        }
    }
}
