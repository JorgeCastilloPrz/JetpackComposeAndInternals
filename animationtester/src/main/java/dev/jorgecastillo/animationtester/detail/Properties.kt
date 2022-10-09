package dev.jorgecastillo.animationtester.detail

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import dev.jorgecastillo.animationtester.FakeSpeakerRepository
import dev.jorgecastillo.animationtester.SpeakerCard

@Composable
fun Properties() {
    var enabled by remember { mutableStateOf(true) }
    val animatedColor = animateColorAsState(
        targetValue = if (enabled) Color.Red else Color.Gray,
        animationSpec = tween(durationMillis = 1000)
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Box(Modifier.drawBehind { drawRect(animatedColor.value) }) {
            SpeakerCard(speaker = FakeSpeakerRepository().getSpeakers()[10])
        }

        Button(onClick = { enabled = !enabled }) {
            Text("Animate border color")
        }
    }
}
