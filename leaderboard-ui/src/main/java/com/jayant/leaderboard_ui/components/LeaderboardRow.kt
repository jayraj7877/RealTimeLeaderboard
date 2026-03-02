package com.jayant.leaderboard_ui.components

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jayant.leaderboard_domain.model.LeaderboardItem

@Composable
fun LeaderboardRow(item: LeaderboardItem) {
    val animatedScore by animateIntAsState(
        targetValue = item.score,
        label = "scoreAnimation"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Text("#${item.rank}", modifier = Modifier.width(40.dp))
        Text(item.username)
        Text(animatedScore.toString())
        Spacer(modifier = Modifier.width(24.dp))
        Text("Score: ${item.score}")
    }
}

