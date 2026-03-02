package com.jayant.leaderboard_ui.screen


import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import com.jayant.leaderboard_ui.components.LeaderboardRow
import com.jayant.leaderboard_ui.viewmodel.LeaderboardViewModel


@Composable
fun LeaderboardScreen(viewModel: LeaderboardViewModel) {
    val items by viewModel.leaderboard.collectAsState()

    LazyColumn {
        items(items, key = { it.playerId }) {
            LeaderboardRow(it)
        }
    }
}