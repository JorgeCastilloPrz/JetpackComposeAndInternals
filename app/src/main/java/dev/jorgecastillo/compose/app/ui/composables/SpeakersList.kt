package dev.jorgecastillo.compose.app.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.jorgecastillo.compose.app.R
import dev.jorgecastillo.compose.app.data.FakeSpeakerRepository
import dev.jorgecastillo.compose.app.models.Speaker
import dev.jorgecastillo.compose.app.ui.theme.ComposeAndInternalsTheme

@Composable
fun SpeakersScreen(speakers: List<Speaker>) {
    Scaffold(topBar = {
        TopAppBar(title = { Text("Speakers") })
    }, floatingActionButton = {
        FloatingActionButton(onClick = { /*TODO*/ }) {
            Icon(
                painter = rememberVectorPainter(image = Icons.Default.Add),
                contentDescription = stringResource(id = R.string.content_desc_fab_add_speaker)
            )
        }
    }, content = { contentPadding ->
        Box(Modifier.padding(contentPadding)) {
            speakers.forEach { speaker ->
                SpeakerCard(speaker)
            }
        }
    })
}

@Composable
fun SpeakerCard(speaker: Speaker) {
    Card {
        Image(
            painter = painterResource(id = R.drawable.avatar_1),
            contentDescription = stringResource(
                id = R.string.content_desc_speaker_avatar,
                speaker.name
            )
        )
    }
}

@Composable
@Preview(showBackground = true)
fun SpeakersScreenPreview() {
    ComposeAndInternalsTheme {
        SpeakersScreen(speakers = FakeSpeakerRepository().getSpeakers())
    }
}
