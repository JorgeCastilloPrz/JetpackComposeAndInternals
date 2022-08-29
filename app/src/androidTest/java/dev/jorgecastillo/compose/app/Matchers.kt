package dev.jorgecastillo.compose.app

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import dev.jorgecastillo.compose.app.Corner.*

fun takesAllAvailableSpace(screenWidth: Int, screenHeight: Int): SemanticsMatcher =
    SemanticsMatcher(
        "${SemanticsProperties.Text.name} takes all available size"
    ) {
        return@SemanticsMatcher it.size.width == screenWidth && it.size.height == screenHeight
    }

fun isCenteredInParent(): SemanticsMatcher =
    SemanticsMatcher(
        "${SemanticsProperties.Text.name} is centered in parent"
    ) {
        val parent = it.parent
        return@SemanticsMatcher if (parent == null) {
            false
        } else {
            val startMargin = it.positionInRoot.x - parent.positionInRoot.x
            val endMargin =
                parent.positionInRoot.x + parent.size.width - (it.positionInRoot.x + it.size.width)

            val topMargin = it.positionInRoot.y - parent.positionInRoot.y
            val bottomMargin =
                parent.positionInRoot.y + parent.size.height - (it.positionInRoot.y + it.size.height)

            (startMargin == endMargin || startMargin == endMargin - 1 || startMargin == endMargin + 1) &&
                topMargin == bottomMargin
        }
    }


fun isAlignedToCorner(corner: Corner): SemanticsMatcher =
    SemanticsMatcher(
        "${SemanticsProperties.Text.name} is aligned to $corner"
    ) {
        val parent = it.parent
        return@SemanticsMatcher if (parent == null) {
            false
        } else {
            when (corner) {
                TopLeft -> it.positionInRoot == Offset(0f, 0f)
                TopRight -> it.positionInRoot == Offset(
                    (parent.size.width - it.size.width).toFloat(),
                    0f
                )
                BottomLeft -> it.positionInRoot == Offset(
                    0f,
                    (parent.size.height - it.size.height).toFloat()
                )
                BottomRight -> it.positionInRoot == Offset(
                    (parent.size.width - it.size.width).toFloat(),
                    (parent.size.height - it.size.height).toFloat()
                )
            }
        }
    }
