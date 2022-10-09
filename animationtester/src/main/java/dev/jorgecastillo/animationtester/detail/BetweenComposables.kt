@file:OptIn(ExperimentalAnimationApi::class)

package dev.jorgecastillo.animationtester.detail

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.jorgecastillo.animationtester.FakeSpeakerRepository
import dev.jorgecastillo.animationtester.SpeakerCard

@Composable
fun BetweenComposables() {
    var isLoading by remember { mutableStateOf(true) }
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Button(onClick = { isLoading = !isLoading }) {
            Text("Toggle loading state")
        }

        AnimatedContent(isLoading) { targetState ->
            if (targetState) {
                CircularProgressIndicator()
            } else {
                SpeakerCard(speaker = FakeSpeakerRepository().getSpeakers()[10])
            }
        }
    }
}
