@file:OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)

package dev.jorgecastillo.animationtester.detail

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.jorgecastillo.animationtester.FakeSpeakerRepository
import dev.jorgecastillo.animationtester.SpeakerCard

@Composable
fun LazyListItemChanges() {
    val speakers = remember { FakeSpeakerRepository().getSpeakers().toMutableStateList() }

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
            item {
                Button(onClick = { speakers.removeFirst() }) {
                    Text("Drop first")
                }
            }
            item { Divider() }
            for (speaker in speakers) {
                item(key = speaker.id) {
                    Box(Modifier.animateItemPlacement()) {
                        SpeakerCard(speaker)
                    }
                }
            }
        }
    }
}
