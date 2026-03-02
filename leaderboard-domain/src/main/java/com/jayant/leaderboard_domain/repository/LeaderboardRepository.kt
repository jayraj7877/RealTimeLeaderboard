package com.jayant.leaderboard_domain.repository

import com.jayant.leaderboard_domain.model.LeaderboardItem
import com.jayant.leaderboard_domain.ranking.calculateRanking
import com.jayant.score_engine.engine.ScoreEngine
import com.jayant.score_engine.model.Player
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.*

class LeaderboardRepository(
    scoreEngine: ScoreEngine,
    players: List<Player>
) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    val leaderboard: StateFlow<List<LeaderboardItem>> =
        scoreEngine.scoreUpdates()
            .scan(players.associate { it.id to 0 }) { acc, event ->
                acc + (event.playerId to event.newScore)
            }
                .map { scores ->
                calculateRanking(
                    players.map { player ->
                        player to scores.getValue(player.id)
                    }
                )
            }
            .stateIn(
                scope = scope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )
}