@file:Suppress("TestFunctionName", "UNCHECKED_CAST", "MoveVariableDeclarationIntoWhen")

package dev.jorgecastillo.compose.app.speakers

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

/**
 * @param viewModel The SpeakersViewModel. You would normally inject it, but we are providing it
 * from the test to focus the exercise on the actual state management.
 */
@Composable
fun SpeakersScreen(viewModel: SpeakersViewModel) {
    val uiState = viewModel.uiState.collectAsState()

    if (uiState.value.isEmpty()) {
        Box(Modifier.fillMaxSize()) {
            Text("No speakers found.")
        }
    } else {
        SpeakersList(speakers = uiState.value)
    }
}

@Composable
fun SpeakersList(speakers: List<Speaker>) {
    Column(
        Modifier
            .testTag("SpeakersList")
            .verticalScroll(rememberScrollState())) {
        speakers.forEach { speaker ->
            SpeakerCard(speaker)
        }
    }
}

@Composable
fun SpeakerCard(speaker: Speaker, onSpeakerClick: (Speaker) -> Unit = {}) {
    Text(
        modifier = Modifier
            .clickable { onSpeakerClick(speaker) }
            .padding(32.dp),
        text = speaker.name,
        style = MaterialTheme.typography.h6
    )
}
