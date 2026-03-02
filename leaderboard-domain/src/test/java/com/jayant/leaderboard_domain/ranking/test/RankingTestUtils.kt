package com.jayant.leaderboard_domain.ranking.test

import com.jayant.leaderboard_domain.model.LeaderboardItem
import com.jayant.score_engine.model.Player

/**
 * Test utilities and helper functions for ranking unit tests.
 */
object RankingTestUtils {

    /**
     * Creates a list of players with sequential scores (high to low).
     *
     * @param count Number of players to create
     * @return List of (Player, Score) pairs with descending scores
     */
    fun createPlayersWithSequentialScores(count: Int): List<Pair<Player, Int>> {
        return (1..count).map { index ->
            Pair(
                Player("player-$index", "Player$index"),
                count - index // Scores: count-1, count-2, ..., 1, 0
            )
        }
    }

    /**
     * Creates a list of players with all the same score.
     *
     * @param count Number of players
     * @param score Score value for all players
     * @return List of (Player, Score) pairs with identical scores
     */
    fun createPlayersWithSameScore(count: Int, score: Int = 100): List<Pair<Player, Int>> {
        return (1..count).map { index ->
            Pair(
                Player("player-$index", "Player$index"),
                score
            )
        }
    }

    /**
     * Creates players with grouped scores (multiple players with same score).
     *
     * Example: createPlayersWithGroupedScores(listOf(3, 2, 1))
     * Creates 3 players with score 100, 2 players with score 90, 1 player with score 80
     *
     * @param groupSizes Sizes of each group (group 1 will have highest score)
     * @return List of (Player, Score) pairs
     */
    fun createPlayersWithGroupedScores(groupSizes: List<Int>): List<Pair<Player, Int>> {
        val result = mutableListOf<Pair<Player, Int>>()
        var playerCount = 0
        var scoreValue = 1000

        for (groupSize in groupSizes) {
            for (i in 0 until groupSize) {
                result.add(
                    Pair(
                        Player("player-${playerCount++}", "Player$playerCount"),
                        scoreValue
                    )
                )
            }
            scoreValue -= 100
        }

        return result
    }

    /**
     * Creates players with random scores.
     *
     * @param count Number of players
     * @param maxScore Maximum possible score
     * @param seed Seed for random generation (for reproducibility)
     * @return List of (Player, Score) pairs
     */
    fun createPlayersWithRandomScores(
        count: Int,
        maxScore: Int = 1000,
        seed: Long = System.currentTimeMillis()
    ): List<Pair<Player, Int>> {
        val random = kotlin.random.Random(seed)
        return (1..count).map { index ->
            Pair(
                Player("player-$index", "Player$index"),
                random.nextInt(maxScore + 1)
            )
        }
    }

    /**
     * Validates that a list of LeaderboardItems is correctly ranked.
     *
     * @param items Items to validate
     * @return Validation result with error message if invalid
     */
    fun validateRankingOrder(items: List<LeaderboardItem>): ValidationResult {
        if (items.isEmpty()) {
            return ValidationResult.Valid
        }

        // Check 1: Scores should be in descending order
        for (i in 0 until items.size - 1) {
            if (items[i].score < items[i + 1].score) {
                return ValidationResult.Invalid(
                    "Scores not in descending order at position $i: " +
                    "${items[i].score} < ${items[i + 1].score}"
                )
            }
        }

        // Check 2: Ranks should be valid and consistent
        var expectedRank = 1
        var previousScore = items[0].score

        for ((index, item) in items.withIndex()) {
            if (item.rank != expectedRank) {
                return ValidationResult.Invalid(
                    "Invalid rank at position $index: expected $expectedRank, got ${item.rank}"
                )
            }

            if (item.score != previousScore) {
                expectedRank = index + 1
                previousScore = item.score
            }
        }

        // Check 3: Ranks should start at 1 and max rank <= size
        if (items.first().rank != 1) {
            return ValidationResult.Invalid("First rank should be 1, got ${items.first().rank}")
        }

        if (items.maxOf { it.rank } > items.size) {
            return ValidationResult.Invalid(
                "Max rank ${items.maxOf { it.rank }} exceeds list size ${items.size}"
            )
        }

        return ValidationResult.Valid
    }

    /**
     * Verifies that ranking handles ties correctly.
     *
     * @param items Items to verify
     * @return true if tie handling is correct
     */
    fun verifyTieHandling(items: List<LeaderboardItem>): Boolean {
        if (items.size < 2) return true

        var playerCountWithCurrentScore = 1
        var expectedNextRank = 2

        for (i in 1 until items.size) {
            if (items[i].score == items[i - 1].score) {
                // Scores are same, should have same rank
                if (items[i].rank != items[i - 1].rank) {
                    return false
                }
                playerCountWithCurrentScore++
            } else {
                // Score changed, next rank should skip appropriately
                expectedNextRank = i + 1
                playerCountWithCurrentScore = 1

                if (items[i].rank != expectedNextRank) {
                    return false
                }
            }
        }

        return true
    }

    /**
     * Extracts leaderboard information as a formatted string.
     * Useful for debugging test failures.
     *
     * @param items Items to format
     * @return Formatted string representation
     */
    fun formatLeaderboard(items: List<LeaderboardItem>): String {
        if (items.isEmpty()) {
            return "Empty leaderboard"
        }

        val header = "Rank | Score | Player\n" + "-".repeat(40)
        val rows = items.joinToString("\n") { item ->
            String.format(
                "%4d | %5d | %s (%s)",
                item.rank,
                item.score,
                item.username,
                item.playerId
            )
        }
        return "$header\n$rows"
    }

    /**
     * Asserts that the leaderboard is correctly ranked.
     * Throws AssertionError if validation fails.
     *
     * @param items Items to validate
     * @param message Custom error message
     */
    fun assertValidRanking(items: List<LeaderboardItem>, message: String = "") {
        val result = validateRankingOrder(items)
        if (result is ValidationResult.Invalid) {
            val errorMsg = buildString {
                if (message.isNotEmpty()) {
                    append("$message\n")
                }
                append("Invalid ranking: ${result.message}\n")
                append(formatLeaderboard(items))
            }
            throw AssertionError(errorMsg)
        }
    }

    /**
     * Gets statistics about a leaderboard.
     *
     * @param items Items to analyze
     * @return Leaderboard statistics
     */
    fun getLeaderboardStats(items: List<LeaderboardItem>): LeaderboardStats {
        return LeaderboardStats(
            totalPlayers = items.size,
            highestScore = items.maxOfOrNull { it.score } ?: 0,
            lowestScore = items.minOfOrNull { it.score } ?: 0,
            averageScore = if (items.isNotEmpty()) items.map { it.score }.average() else 0.0,
            maxRank = items.maxOfOrNull { it.rank } ?: 0,
            tiedGroupCount = items.groupBy { it.score }.count()
        )
    }

    /**
     * Validation result sealed class.
     */
    sealed class ValidationResult {
        object Valid : ValidationResult()
        data class Invalid(val message: String) : ValidationResult()
    }

    /**
     * Statistics about a leaderboard.
     */
    data class LeaderboardStats(
        val totalPlayers: Int,
        val highestScore: Int,
        val lowestScore: Int,
        val averageScore: Double,
        val maxRank: Int,
        val tiedGroupCount: Int
    ) {
        override fun toString(): String {
            return """
                Leaderboard Statistics
                ----------------------
                Total Players: $totalPlayers
                Highest Score: $highestScore
                Lowest Score: $lowestScore
                Average Score: ${"%.2f".format(averageScore)}
                Max Rank: $maxRank
                Tied Groups: $tiedGroupCount
            """.trimIndent()
        }
    }
}

