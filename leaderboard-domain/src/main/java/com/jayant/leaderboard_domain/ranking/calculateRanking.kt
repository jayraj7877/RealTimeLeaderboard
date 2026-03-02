package com.jayant.leaderboard_domain.ranking

import com.jayant.leaderboard_domain.model.LeaderboardItem
import com.jayant.score_engine.model.Player

fun calculateRanking(
    players: List<Pair<Player, Int>>
): List<LeaderboardItem> {

    val sorted = players.sortedByDescending { it.second }

    var lastScore: Int? = null
    var rank = 0

    return sorted.mapIndexed { index, (player, score) ->
        if (score != lastScore) {
            rank = index + 1
            lastScore = score
        }

        LeaderboardItem(
            playerId = player.id,
            username = player.name,
            score = score,
            rank = rank
        )
    }
}