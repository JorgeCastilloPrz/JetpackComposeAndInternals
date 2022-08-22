package dev.jorgecastillo.compose.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Sample Counter composable to debug recomposition. Whenever we click the button, only the
 * button and the text will recompose, since they are the only two Composable functions or
 * Composable lambdas here that are reading from that State. The Counter composable does not,
 * and therefore it does not need to recompose.
 *
 * Recomposition is highlighted using the [Modifier.recomposeHighlighter] created by the Jetpack
 * Compose team (Google).
 */
@Composable
fun Counter() {
    RecompositionBox {
        var counter by remember { mutableStateOf(0) }

        RecompositionButton(onClick = { counter++ }) {
            RecompositionText(text = "Counter: $counter")
        }
    }
}

@Composable
fun RecompositionBox(content: @Composable BoxScope.() -> Unit) {
    Box(
        Modifier
            .recomposeHighlighter()
            .padding(16.dp)
    ) {
        content()
    }
}

@Composable
fun RecompositionButton(onClick: () -> Unit, content: @Composable RowScope.() -> Unit) {
    Button(
        modifier = Modifier
            .recomposeHighlighter()
            .fillMaxWidth()
            .size(300.dp)
            .background(Color.Gray),
        onClick = { onClick() }
    ) {
        content()
    }
}

@Composable
fun RecompositionText(text: String) {
    Text(
        modifier = Modifier
            .background(Color.Magenta)
            .recomposeHighlighter(),
        text = text,
        style = MaterialTheme.typography.h6
    )
}
