package dev.jorgecastillo.compose.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import dev.jorgecastillo.compose.app.ui.theme.ComposeAndInternalsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeAndInternalsTheme {
                HelloWorld()
            }
        }
    }
}

@Composable
fun HelloWorld() {
    rememberCoroutineScope()
    Text("Hello world!")
}

@Composable
@Preview(showBackground = true)
fun MainScreenPreview() {
    ComposeAndInternalsTheme {
        HelloWorld()
    }
}
