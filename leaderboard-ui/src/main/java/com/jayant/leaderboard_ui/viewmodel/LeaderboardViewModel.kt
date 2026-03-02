package com.jayant.leaderboard_ui.viewmodel

import androidx.lifecycle.ViewModel
import com.jayant.leaderboard_domain.repository.LeaderboardRepository

class LeaderboardViewModel(
    repository: LeaderboardRepository
) : ViewModel() {
    val leaderboard = repository.leaderboard
}