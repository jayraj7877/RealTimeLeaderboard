package com.jayant.realtimeleaderboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.jayant.leaderboard_domain.repository.LeaderboardRepository
import com.jayant.leaderboard_ui.screen.LeaderboardScreen
import com.jayant.leaderboard_ui.viewmodel.LeaderboardViewModel
import com.jayant.score_engine.ScoreGenerator
import com.jayant.score_engine.model.Player

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val players = List(20) {
            Player("id$it", "Player $it")
        }

        val engine = ScoreGenerator(players)
        val repository = LeaderboardRepository(engine, players)
        val viewModel = LeaderboardViewModel(repository)

        setContent {
            LeaderboardScreen(viewModel)
        }
    }
}