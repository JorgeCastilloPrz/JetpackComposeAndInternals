@file:Suppress("TestFunctionName")

package dev.jorgecastillo.compose.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.jorgecastillo.compose.app.ui.theme.ComposeAndInternalsTheme

/**
 * ### Exercise 6 👩🏾‍💻
 *
 * This exercise explores the drawing APIs from the [DrawScope]. We want to implement the circled
 * variant of the Modifier.rainbowBorder show in the slides.
 *
 * As a reference, check the screenshots/exercise6.png image.
 *
 * To complete this exercise:
 *
 * 1. Build a custom modifier that relies on [Modifier.drawWithContent].
 * 2. draw a white circle for the background.
 * 3. draw the content.
 * 4. draw a circle with a brush using a linear gradient with the colors [Color.Magenta, Color.Cyan]
 * 5. make the circle size match the canvas size
 * 6. set the stroke width following the strokeWidth parameter
 *
 * This exercise is not validated. You can validate your own implementation by checking the
 * reference image. If it does not match the image, feel free to post your questions in the channel.
 */
fun Modifier.circledRainbowBorder(strokeWidth: Float): Modifier =
    drawWithContent {
        
    }

@Preview
@Composable
private fun CircledRainbowBorderPreview() {
    ComposeAndInternalsTheme {
        Box(Modifier.circledRainbowBorder(24f).padding(36.dp)) {
            Text("Hey")
        }
    }
}
