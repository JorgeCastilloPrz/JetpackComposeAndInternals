@file:Suppress("TestFunctionName", "PrivatePropertyName")

package dev.jorgecastillo.compose.app

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import dev.jorgecastillo.compose.app.ui.theme.Shapes
import dev.jorgecastillo.compose.app.ui.theme.Typography

private val DarkColors = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200
)

private val LightColors = lightColors(
    primary = Purple500,
    primaryVariant = Purple700,
    secondary = Teal200
)

private val DarkPinkColors = darkColors(
    primary = Pink200,
    primaryVariant = Pink700,
    secondary = Teal200,
    surface = Pink200
)

private val LightPinkColors = lightColors(
    primary = Pink500,
    primaryVariant = Pink700,
    secondary = Teal200,
    surface = Pink700
)

@Composable
fun MyAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColors
    } else {
        LightColors
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

@Composable
fun PinkTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkPinkColors
    } else {
        LightPinkColors
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
