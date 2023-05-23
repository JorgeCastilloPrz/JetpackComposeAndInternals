package dev.jorgecastillo.compose.app.ui.composables.lazylayout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CustomLazyLayoutSample() {
    CustomLazyLayout {
        val myItems = (1..100).map {
            ListItem(
                (-3000..3000).random(),
                (-3000..3000).random(),
            )
        }.toList()
        items(myItems) { item ->
            Text(
                modifier = Modifier
                    .background(Color.Red)
                    .padding(8.dp),
                text = "(${item.x}, ${item.y})"
            )
        }
    }
}
