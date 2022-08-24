package dev.jorgecastillo.compose.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.jorgecastillo.compose.app.ui.composables.NamePlate
import dev.jorgecastillo.compose.app.ui.theme.ComposeAndInternalsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeAndInternalsTheme {
                NamePlate("John Doe")
            }
        }
    }
}
