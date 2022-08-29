package dev.jorgecastillo.compose.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import dev.jorgecastillo.compose.app.data.FakeSpeakerRepository
import dev.jorgecastillo.compose.app.ui.composables.LazySpeakersScreen
import dev.jorgecastillo.compose.app.ui.theme.ComposeAndInternalsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val speakersRepository = FakeSpeakerRepository()
            ComposeAndInternalsTheme {
//                LazySpeakersScreen(speakers = speakersRepository.getSpeakers())
                StairedBox {
                    Text("Text 1")
                    Text("Text 2")
                    Text("Text 3")
                    Text("Text 4")
                    Text("Text 5")
                    Text("Text 6")
                }
            }
        }
    }
}
@Composable
fun StairedBox(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(modifier = modifier, content = content ) {
            measurables, constraints ->

        val placeables = measurables.map { measurable ->
            measurable.measure(constraints)
        }

        layout(constraints.maxWidth, constraints.maxHeight) {
            // Track the x and y coord
            var xPosition = 0
            var yPosition = 0

            placeables.forEach { placeable ->
                placeable.placeRelative(x = xPosition, y = yPosition)
                xPosition += placeable.width
                yPosition += placeable.height
            }
        }
    }
}
