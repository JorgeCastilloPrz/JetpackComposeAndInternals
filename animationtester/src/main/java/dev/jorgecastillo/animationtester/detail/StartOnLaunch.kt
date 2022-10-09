@file:OptIn(ExperimentalMaterialApi::class)

package dev.jorgecastillo.animationtester.detail

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInElastic
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.EaseOutBounce
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import dev.jorgecastillo.animationtester.FakeSpeakerRepository
import dev.jorgecastillo.animationtester.SpeakerCard

@Composable
fun StartOnLaunch() {
    val alphaAnimation = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        alphaAnimation.animateTo(
            targetValue = 1f,
            tween(
                1000,
                delayMillis = 500,
                easing = EaseInOut
            )
        )
    }

    Box(Modifier.alpha(alphaAnimation.value)) {
        SpeakerCard(speaker = FakeSpeakerRepository().getSpeakers()[10])
    }
}
