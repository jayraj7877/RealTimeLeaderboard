package com.jayant.score_engine

import com.jayant.score_engine.engine.ScoreEngine
import com.jayant.score_engine.model.Player
import com.jayant.score_engine.model.ScoreEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlin.random.Random
import kotlinx.coroutines.isActive

class ScoreGenerator(
    private val players: List<Player>,
    seed: Long = System.currentTimeMillis()
) : ScoreEngine {

    private val random = Random(seed)
    private val scores = players.associate { it.id to 0 }.toMutableMap()

    override fun scoreUpdates(): Flow<ScoreEvent> = flow {
        while (currentCoroutineContext().isActive) {
            delay(random.nextLong(500, 2000))

            val player = players.random(random)
            val increment = random.nextInt(5, 20)

            val newScore = scores.getValue(player.id) + increment
            scores[player.id] = newScore

            emit(ScoreEvent(player.id, newScore))
        }
    }.flowOn(Dispatchers.Default)
}