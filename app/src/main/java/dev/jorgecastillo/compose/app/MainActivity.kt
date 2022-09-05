package dev.jorgecastillo.compose.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
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
                Box(contentAlignment = Alignment.Center) {
                   Icon(
                        painter = rememberVectorPainter(image = Icons.Default.Add),
                        contentDescription = null,
                        tint = Color.Red
                    )
                }
            }
        }
    }
}
