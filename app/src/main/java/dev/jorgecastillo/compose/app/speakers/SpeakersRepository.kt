package dev.jorgecastillo.compose.app.speakers

class SpeakersRepository {

    /**
     * Stubbed response. Would normally load from network (that is why it is suspend).
     * @return A list of speakers.
     */
    suspend fun loadSpeakers(): List<Speaker> = listOf(
        Speaker("1", "Jane Smith"),
        Speaker("2", "Aleesha Salgado"),
        Speaker("3", "Alayna Bradley"),
        Speaker("4", "Zunaira English"),
        Speaker("5", "Cassandra Higgins"),
        Speaker("6", "Olivia"),
        Speaker("7", "Emma"),
        Speaker("8", "Charlotte"),
        Speaker("9", "Amelia"),
        Speaker("10", "Ava"),
        Speaker("11", "Ada"),
        Speaker("12", "Sophia"),
        Speaker("13", "Isabella"),
        Speaker("14", "Mia"),
        Speaker("15", "Nora")
    )
}
