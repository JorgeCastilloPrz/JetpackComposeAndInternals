package dev.jorgecastillo.compose.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import dev.jorgecastillo.compose.app.data.FakeSpeakerRepository
import dev.jorgecastillo.compose.app.ui.theme.ComposeAndInternalsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val speakersRepository = FakeSpeakerRepository()
            ComposeAndInternalsTheme {
//                LazySpeakersScreen(speakers = speakersRepository.getSpeakers())
                Box(Modifier.fillMaxWidth().background(Color.Yellow)) {
                    Button(modifier = Modifier.takeHalfParentWidthAndCenter(), onClick = {}) {
                        Text("Hello world!")
                    }
                }
            }
        }
    }
}

fun Modifier.takeHalfParentWidthAndCenter(): Modifier =
    this.layout { measurable, constraints ->
        val maxWidthAllowedByParent = constraints.maxWidth
        val placeable = measurable.measure(constraints.copy(minWidth = maxWidthAllowedByParent / 2))
        layout(placeable.width, placeable.height) {
            // Where the composable gets placed
            placeable.placeRelative(maxWidthAllowedByParent / 2 - placeable.width / 2, 0)
        }
    }
