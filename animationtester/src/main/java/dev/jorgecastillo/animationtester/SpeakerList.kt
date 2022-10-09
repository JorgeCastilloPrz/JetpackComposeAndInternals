@file:OptIn(ExperimentalMaterialApi::class)

package dev.jorgecastillo.animationtester

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import dev.jorgecastillo.animationtester.ui.theme.JetpackComposeAndInternalsTheme

@Composable
fun SpeakerList(speakers: List<Speaker>) {
    LazyColumn {
        items(speakers) { speaker ->
            SpeakerCard(speaker)
        }
    }
}

@Composable
fun SpeakerCard(speaker: Speaker, onClick: (Speaker) -> Unit = {}) {
    Card(
        onClick = { onClick(speaker) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.spacing_small))
    ) {
        Row(Modifier.padding(dimensionResource(id = R.dimen.spacing_regular))) {
            Image(
                painter = painterResource(R.drawable.avatar_10),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.avatar_size))
                    .clip(CircleShape)
            )
            Column(Modifier.padding(start = dimensionResource(id = R.dimen.spacing_regular))) {
                Text(text = speaker.name, style = MaterialTheme.typography.h6)
                Text(text = speaker.company, style = MaterialTheme.typography.caption)
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun LazySpeakersScreenPreview() {
    JetpackComposeAndInternalsTheme {
        SpeakerList(speakers = FakeSpeakerRepository().getSpeakers())
    }
}

