@file:Suppress("TestFunctionName")

package dev.jorgecastillo.compose.app

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
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import dev.jorgecastillo.compose.app.models.Speaker
import dev.jorgecastillo.compose.app.ui.composables.avatarResForId

@Composable
fun Feed(speakers: List<Speaker>) {
    Scaffold(topBar = {
        TopAppBar(title = { Text("Speakers") })
    }, floatingActionButton = {
        FloatingActionButton(onClick = { /*TODO*/ }) {
            Icon(
                painter = rememberVectorPainter(image = Icons.Default.Add),
                contentDescription = null
            )
        }
    }, content = { contentPadding ->
        LazyColumn(
            Modifier
                .padding(contentPadding)
                .testTag("SpeakersList")
        ) {
            items(speakers) { speaker ->
                if (speaker.company == "Lyft") {
                    PinkTheme {
                        SpeakerCard(speaker)
                    }
                } else {
                    SpeakerCard(speaker)
                }
            }
        }
    })
}

@Composable
fun SpeakerCard(speaker: Speaker) {
    Card(
        Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.spacing_small))
    ) {
        Row(Modifier.padding(dimensionResource(id = R.dimen.spacing_regular))) {
            Image(
                painter = painterResource(avatarResForId(speaker.id)),
                contentDescription = stringResource(
                    id = R.string.content_desc_speaker_avatar,
                    speaker.name
                ),
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
