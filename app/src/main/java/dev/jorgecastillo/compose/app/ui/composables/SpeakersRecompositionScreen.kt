package dev.jorgecastillo.compose.app.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import dev.jorgecastillo.compose.app.R
import dev.jorgecastillo.compose.app.data.FakeSpeakerRepository
import dev.jorgecastillo.compose.app.models.Speaker
import dev.jorgecastillo.compose.app.recomposeHighlighter
import dev.jorgecastillo.compose.app.ui.theme.ComposeAndInternalsTheme
import dev.jorgecastillo.compose.app.viewmodel.SpeakersViewModel

@Composable
fun SpeakersRecompositionScreen() {
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
        val viewModel: SpeakersViewModel = viewModel()
        val uiState = viewModel.uiState.collectAsState()
        val state = uiState.value

        SwipeRefresh(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize(),
            state = rememberSwipeRefreshState(state.isLoading),
            onRefresh = { viewModel.onPullToRefresh() },
        ) {
            SpeakersRecompositionScreen(state.speakers)
        }
    })
}

@Composable
fun SpeakersRecompositionScreen(speakers: List<Speaker>) {
    Column(
        Modifier
            .verticalScroll(rememberScrollState())
            .testTag("SpeakersList")
            .recomposeHighlighter()
    ) {
        speakers.forEach { speaker ->
            SpeakerCard(speaker)
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun SpeakersScreenPreview() {
    ComposeAndInternalsTheme {
        SpeakersScreen(speakers = FakeSpeakerRepository().getSpeakers())
    }
}
