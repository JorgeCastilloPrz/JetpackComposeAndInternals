package dev.jorgecastillo.compose.app.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.jorgecastillo.compose.app.R
import dev.jorgecastillo.compose.app.data.FakeSpeakerRepository
import dev.jorgecastillo.compose.app.data.SpeakerRepository
import dev.jorgecastillo.compose.app.ui.theme.ComposeAndInternalsTheme

@Composable
fun SpeakerProfileScreen(speakerId: String?, repo: SpeakerRepository = FakeSpeakerRepository()) {
    val speaker = speakerId?.let { repo.getSpeakerById(it) }
    if (speaker == null) {
        Box(contentAlignment = Alignment.Center) {
            Text("Speaker not found!")
        }
    } else {
        Card(
            Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(dimensionResource(id = R.dimen.spacing_small))
        ) {
            Column {
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
                        Button(onClick = { /*TODO*/ }) {
                            Text("Follow")
                        }
                    }
                }

                Divider(modifier = Modifier.padding(dimensionResource(id = R.dimen.spacing_regular)))

                Text(
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.spacing_regular)),
                    text = stringResource(id = R.string.lorem_ipsum_text)
                )

                Divider(modifier = Modifier.padding(dimensionResource(id = R.dimen.spacing_regular)))

                Text(
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.spacing_regular)),
                    text = stringResource(id = R.string.lorem_ipsum_text)
                )

                Divider(modifier = Modifier.padding(dimensionResource(id = R.dimen.spacing_regular)))

                Text(
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.spacing_regular)),
                    text = stringResource(id = R.string.lorem_ipsum_text)
                )
            }
        }
    }
}

@Composable
@Preview
fun SpeakerProfilePreview() {
    ComposeAndInternalsTheme {
        SpeakerProfileScreen("10", FakeSpeakerRepository())
    }
}
