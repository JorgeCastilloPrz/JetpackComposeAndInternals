package dev.jorgecastillo.animationtester.detail

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import dev.jorgecastillo.animationtester.FakeSpeakerRepository
import dev.jorgecastillo.animationtester.SpeakerCard

@Composable
fun VisibilityViaProperties() {
    var editable by remember { mutableStateOf(true) }
    val animatedAlpha: Float by animateFloatAsState(
        if (editable) 1f else 0f,
        animationSpec = tween(1500)
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Box(Modifier.graphicsLayer { alpha = animatedAlpha }) {
            SpeakerCard(speaker = FakeSpeakerRepository().getSpeakers()[10])
        }

        Button(onClick = { editable = !editable }) {
            Text("Toggle visibility")
        }
    }
}
