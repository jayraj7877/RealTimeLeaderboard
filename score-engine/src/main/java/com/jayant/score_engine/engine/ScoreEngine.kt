package com.jayant.score_engine.engine

import com.jayant.score_engine.model.ScoreEvent
import kotlinx.coroutines.flow.Flow


interface ScoreEngine {
    fun scoreUpdates(): Flow<ScoreEvent>
}