package com.jayant.leaderboard_domain.model

data class LeaderboardItem(
    val playerId: String,
    val username: String,
    val score: Int,
    val rank: Int
)
