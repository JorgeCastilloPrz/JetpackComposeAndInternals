package dev.jorgecastillo.compose.app.ui.composables

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import dev.jorgecastillo.compose.app.data.FakeSpeakerRepository
import dev.jorgecastillo.compose.app.data.SpeakerRepository
import dev.jorgecastillo.compose.app.models.Speaker
import dev.jorgecastillo.compose.app.ui.theme.ComposeAndInternalsTheme

@Composable
fun SpeakerFeed(
    modifier: Modifier = Modifier,
    repo: SpeakerRepository = FakeSpeakerRepository(),
    onSpeakerClick: (Speaker) -> Unit = {}
) {
    val speakers = repo.getSpeakers()

    LazyColumn(modifier.testTag("SpeakersList")) {
        items(speakers) { speaker ->
            SpeakerCard(speaker) { onSpeakerClick(speaker) }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun LazySpeakersScreenPreview() {
    ComposeAndInternalsTheme {
        SpeakerFeed { }
    }
}
