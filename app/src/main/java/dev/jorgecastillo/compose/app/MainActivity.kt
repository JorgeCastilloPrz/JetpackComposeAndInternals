@file:OptIn(ExperimentalAnimationGraphicsApi::class)

package dev.jorgecastillo.compose.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.Button
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import dev.jorgecastillo.compose.app.data.FakeSpeakerRepository
import dev.jorgecastillo.compose.app.ui.composables.LazySpeakersScreen
import dev.jorgecastillo.compose.app.ui.theme.ComposeAndInternalsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val speakersRepository = FakeSpeakerRepository()
            MaterialTheme {
//                LazySpeakersScreen(speakers = speakersRepository.getSpeakers())
                Scaffold(
                    topBar = { TopAppBar(title = { Text("My app") }) },
                    floatingActionButton = {
                        FloatingActionButton(onClick = { /*TODO*/ }) {
                            Icon(
                                painter = rememberVectorPainter(image = Icons.Default.Add),
                                contentDescription = null
                            )
                        }
                    },
                    content = { contentPadding ->
                        Column(
                            Modifier
                                .padding(contentPadding)
                                .padding(16.dp)
                        ) {
                            TextField(
                                value = "",
                                label = { Text("Insert some text") },
                                onValueChange = {})
                            Button(onClick = { /*TODO*/ }) {
                                Text("Click me!")
                            }
                        }
                    }
                )
            }
        }
    }
}
