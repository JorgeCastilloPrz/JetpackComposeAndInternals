package dev.jorgecastillo.compose.app

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher

fun takesAllAvailableSpace(screenWidth: Int, screenHeight: Int): SemanticsMatcher =
    SemanticsMatcher(
        "${SemanticsProperties.Text.name} takes all available size"
    ) {
        return@SemanticsMatcher it.size.width == screenWidth && it.size.height == screenHeight
    }

fun isCenteredInParent(): SemanticsMatcher =
    SemanticsMatcher(
        "${SemanticsProperties.Text.name} takes all available size"
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
