package dev.jorgecastillo.compose.app.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.jorgecastillo.compose.app.ui.theme.ComposeAndInternalsTheme

@Composable
fun NamePlateWithThemeSupport(name: String) {
    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface),
        contentAlignment = Alignment.Center
    ) {
        Text(name, color = MaterialTheme.colors.onSurface)
    }
}

@Composable
@Preview(showBackground = true)
fun NamePlateWithThemeSupportPreview() {
    ComposeAndInternalsTheme {
        NamePlateWithThemeSupport(name = "Test name")
    }
}
