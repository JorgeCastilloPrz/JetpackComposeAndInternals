@file:OptIn(ExperimentalAnimationGraphicsApi::class, ExperimentalTime::class)

package dev.jorgecastillo.compose.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.jorgecastillo.compose.app.data.FakeSpeakerRepository
import dev.jorgecastillo.compose.app.ui.composables.lazypane.LazyPane
import dev.jorgecastillo.compose.app.ui.composables.lazypane.items
import dev.jorgecastillo.compose.app.ui.theme.ComposeAndInternalsTheme
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val speakersRepository = FakeSpeakerRepository()
            ComposeAndInternalsTheme {
                Surface {
                    LazyGrid(epgData(), Modifier.fillMaxSize())
                }
//                LazySpeakersScreen(speakers = speakersRepository.getSpeakers())
            }
        }
    }

}

@Composable
fun LazyGrid(epgData: List<List<EpgCell>>, modifier: Modifier = Modifier) {
    // Approach 1: sharing horizontal scrolling state across all rows so they scroll together
    // Issues: Low performance (no recycling)
    //           OOM when composing so many items (no lazy layout / subcomposition)
//    val horizontalScrollState = rememberScrollState()
//    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
//        for (channelData in epgData) {
//            Row(modifier = Modifier.horizontalScroll(horizontalScrollState)) {
//                for (epgCell in channelData) {
//                    EpgCellCard(epgCell)
//                }
//            }
//        }
//    }

    // Approach 2: apply scrolling in both directions to container
    // Issues: Low performance (no recycling)
    //         OOM when composing so many items (no lazy layout / subcomposition)
    //         All content scrolls together horizontally, so a row that has no more elements will
    //         keep scrolling if there are longer rows in the column, since they can still scroll
    //         This makes the option above better, where rows with no more elements stop scrolling
    //         while others with more elements still do.
//    val scrollStateHorizontal = rememberScrollState()
//    val scrollStateVertical = rememberScrollState()
//
//    Column(
//        modifier = Modifier
//            .horizontalScroll(scrollStateHorizontal)
//            .verticalScroll(scrollStateVertical)
//    ) {
//        for (channelData in epgData) {
//            Row {
//                for (epgCell in channelData) {
//                    EpgCellCard(epgCell)
//                }
//            }
//        }
//    }

    // List<List<EpgCell> as a list of rows
    LazyPane(modifier) {
        items(epgData) { epgCell ->
            EpgCellCard(epgCell)
        }
    }
}

@Composable
fun EpgCellCard(cell: EpgCell) {
    Card(
        modifier = Modifier.padding(8.dp)
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = cell.programTitle,
            maxLines = 1
        )
    }
}

data class EpgCell(
    val programTitle: String,
    val duration: Duration = 180.toDuration(DurationUnit.MINUTES)
)

@Composable
fun Duration.dp() = this.inWholeMinutes.toInt().dp

fun epgData() = listOf(
    (0..10).map { EpgCell("Some interesting title for this item $it in row 0") },
    (0..15).map { EpgCell("Some interesting title for this item $it in row 1") },
    (0..20).map { EpgCell("Some interesting title for this item $it in row 2") },
    (0..5).map { EpgCell("Some interesting title for this item $it in row 3") },
    (0..30).map { EpgCell("Some interesting title for this item $it in row 4") },
    (0..20).map { EpgCell("Some interesting title for this item $it in row 5") },
    (0..14).map { EpgCell("Some interesting title for this item $it in row 6") },
    (0..8).map { EpgCell("Some interesting title for this item $it in row 7") },
    (0..3).map { EpgCell("Some interesting title for this item $it in row 8") },
    (0..17).map { EpgCell("Some interesting title for this item $it in row 9") },
    (0..22).map { EpgCell("Some interesting title for this item $it in row 10") },
    (0..31).map { EpgCell("Some interesting title for this item $it in row 11") },
    (0..12).map { EpgCell("Some interesting title for this item $it in row 12") },
    (0..3).map { EpgCell("Some interesting title for this item $it in row 13") },
    (0..17).map { EpgCell("Some interesting title for this item $it in row 14") },
    (0..22).map { EpgCell("Some interesting title for this item $it in row 15") },
    (0..31).map { EpgCell("Some interesting title for this item $it in row 16") },
    (0..12).map { EpgCell("Some interesting title for this item $it in row 17") }
)