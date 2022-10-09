@file:OptIn(ExperimentalMaterialApi::class)

package dev.jorgecastillo.animationtester.detail

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import dev.jorgecastillo.animationtester.FakeSpeakerRepository
import dev.jorgecastillo.animationtester.SpeakerCard

@Composable
fun SizeChanges() {
    var expanded by remember { mutableStateOf(true) }
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.animateContentSize()) {
            SpeakerCard(speaker = FakeSpeakerRepository().getSpeakers()[10])
            if (expanded) {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = "Some description about this person because we want to showcase how the " +
                        "animateContentSize modifier works, which is visually interesting. It can be used " +
                        "to expand and collapse items like this one, for example."
                )
            }
        }

        Button(onClick = { expanded = !expanded }) {
            Text("Toggle to expand/collapse")
        }
    }
}
