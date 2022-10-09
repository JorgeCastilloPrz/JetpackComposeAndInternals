package dev.jorgecastillo.animationtester

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import dev.jorgecastillo.animationtester.ui.theme.JetpackComposeAndInternalsTheme

class TestAnimationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackComposeAndInternalsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {
                    AnimationTester { clickedOption -> navigateToOption(clickedOption) }
                }
            }
        }
    }

    private fun navigateToOption(option: Option) {
        val intent = Intent(this, AnimationDetail::class.java)
        intent.putExtra(AnimationDetail.EXTRAS, option)
        startActivity(intent)
    }
}
