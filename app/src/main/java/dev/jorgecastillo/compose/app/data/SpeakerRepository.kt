package dev.jorgecastillo.compose.app.data

import dev.jorgecastillo.compose.app.models.Speaker

interface SpeakerRepository {

    fun getSpeakers(): List<Speaker>

    fun getSpeakerById(id: String): Speaker?
}
