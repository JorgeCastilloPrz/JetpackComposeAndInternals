@file:OptIn(ExperimentalMaterialApi::class) @file:Suppress(
    "UpdateTransitionLabel", "TransitionPropertiesLabel"
)

package dev.jorgecastillo.animationtester.detail

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

enum class State { Collapsed, Expanded }

@Composable
fun MultipleProperties() {
    var enabled by remember { mutableStateOf(false) }
    val transition = updateTransition(enabled)

    val borderWidth by transition.animateDp { isEnabled ->
        if (isEnabled) 24.dp else 8.dp
    }

    val color by transition.animateColor { isEnabled ->
        if (isEnabled) MaterialTheme.colors.primary else MaterialTheme.colors.secondary
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .size(128.dp)
                .border(borderWidth, color, CircleShape)
        )

        Button(onClick = { enabled = !enabled }) {
            Text("Toggle state")
        }
    }
}
