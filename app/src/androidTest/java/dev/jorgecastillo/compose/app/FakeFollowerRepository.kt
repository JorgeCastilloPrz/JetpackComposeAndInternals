package dev.jorgecastillo.compose.app

class FakeFollowerRepository {

    fun getFollowers(): List<Follower> = listOf(
        Follower("John Doe", "United States"),
        Follower("Sylvia Lotte", "Italy"),
        Follower("Apis Anoubis", "Egypt"),
        Follower("Aeolus Phrixos", "South Africa"),
        Follower("Oz David", "Thailand"),
    )
}