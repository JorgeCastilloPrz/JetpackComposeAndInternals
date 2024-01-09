package dev.jorgecastillo.compose.app.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.jorgecastillo.compose.app.ui.theme.ComposeAndInternalsTheme

@Composable
fun NamePlate(name: String) {

}

@Composable
@Preview(showBackground = true)
fun NamePlatePreview() {
    ComposeAndInternalsTheme {
        NamePlate(name = "Test name")
    }
}
