package com.jayant.leaderboard_domain.ranking

import com.jayant.leaderboard_domain.model.LeaderboardItem
import com.jayant.score_engine.model.Player
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CalculateRankingTest {

    private lateinit var testPlayers: List<Pair<Player, Int>>

    @Before
    fun setup() {
        testPlayers = emptyList()
    }

    // ==================== Basic Ranking Tests ====================

    @Test
    fun testCalculateRanking_EmptyList() {
        // Given: Empty list of players
        val players: List<Pair<Player, Int>> = emptyList()

        // When: Calculate ranking
        val result = calculateRanking(players)

        // Then: Result should be empty
        assertEquals(0, result.size)
    }

    @Test
    fun testCalculateRanking_SinglePlayer() {
        // Given: Single player
        val player = Player("1", "Player1")
        val players = listOf(Pair(player, 100))

        // When: Calculate ranking
        val result = calculateRanking(players)

        // Then: Player should have rank 1
        assertEquals(1, result.size)
        assertEquals(1, result[0].rank)
        assertEquals("Player1", result[0].username)
        assertEquals(100, result[0].score)
    }

    @Test
    fun testCalculateRanking_TwoPlayersDescendingOrder() {
        // Given: Two players with different scores in descending order
        val player1 = Player("1", "Alice")
        val player2 = Player("2", "Bob")
        val players = listOf(
            Pair(player1, 100),
            Pair(player2, 50)
        )

        // When: Calculate ranking
        val result = calculateRanking(players)

        // Then: Verify correct ranking
        assertEquals(2, result.size)
        assertEquals(1, result[0].rank)
        assertEquals("Alice", result[0].username)
        assertEquals(100, result[0].score)
        assertEquals(2, result[1].rank)
        assertEquals("Bob", result[1].username)
        assertEquals(50, result[1].score)
    }

    // ==================== Sorting Tests ====================

    @Test
    fun testCalculateRanking_UnsortedInput() {
        // Given: Players in random order
        val players = listOf(
            Pair(Player("1", "Alice"), 50),
            Pair(Player("2", "Bob"), 150),
            Pair(Player("3", "Charlie"), 100)
        )

        // When: Calculate ranking
        val result = calculateRanking(players)

        // Then: Should be sorted by score descending
        assertEquals(3, result.size)
        assertEquals("Bob", result[0].username)
        assertEquals(150, result[0].score)
        assertEquals(1, result[0].rank)
        assertEquals("Charlie", result[1].username)
        assertEquals(100, result[1].score)
        assertEquals(2, result[1].rank)
        assertEquals("Alice", result[2].username)
        assertEquals(50, result[2].score)
        assertEquals(3, result[2].rank)
    }

    // ==================== Tie Tests ====================

    @Test
    fun testCalculateRanking_TwoPlayersSameScore() {
        // Given: Two players with the same score
        val player1 = Player("1", "Alice")
        val player2 = Player("2", "Bob")
        val players = listOf(
            Pair(player1, 100),
            Pair(player2, 100)
        )

        // When: Calculate ranking
        val result = calculateRanking(players)

        // Then: Both should have rank 1
        assertEquals(2, result.size)
        assertEquals(1, result[0].rank)
        assertEquals("Alice", result[0].username)
        assertEquals(1, result[1].rank)
        assertEquals("Bob", result[1].username)
    }

    @Test
    fun testCalculateRanking_MultipleTies() {
        // Given: Multiple players with tied scores
        val players = listOf(
            Pair(Player("1", "Alice"), 100),
            Pair(Player("2", "Bob"), 100),
            Pair(Player("3", "Charlie"), 80),
            Pair(Player("4", "Diana"), 80),
            Pair(Player("5", "Eve"), 80)
        )

        // When: Calculate ranking
        val result = calculateRanking(players)

        // Then: Verify correct rank assignment with ties
        assertEquals(5, result.size)
        // First two tied at rank 1
        assertEquals(1, result[0].rank)
        assertEquals(100, result[0].score)
        assertEquals(1, result[1].rank)
        assertEquals(100, result[1].score)
        // Next three tied at rank 3 (skip rank 2)
        assertEquals(3, result[2].rank)
        assertEquals(80, result[2].score)
        assertEquals(3, result[3].rank)
        assertEquals(80, result[3].score)
        assertEquals(3, result[4].rank)
        assertEquals(80, result[4].score)
    }

    @Test
    fun testCalculateRanking_AllPlayersSameScore() {
        // Given: All players with the same score
        val players = listOf(
            Pair(Player("1", "Alice"), 50),
            Pair(Player("2", "Bob"), 50),
            Pair(Player("3", "Charlie"), 50)
        )

        // When: Calculate ranking
        val result = calculateRanking(players)

        // Then: All should have rank 1
        assertEquals(3, result.size)
        for (item in result) {
            assertEquals(1, item.rank)
            assertEquals(50, item.score)
        }
    }

    // ==================== Data Integrity Tests ====================

    @Test
    fun testCalculateRanking_PlayerIdPreserved() {
        // Given: Players with specific IDs
        val playerId = "player-123"
        val player = Player(playerId, "TestPlayer")
        val players = listOf(Pair(player, 100))

        // When: Calculate ranking
        val result = calculateRanking(players)

        // Then: Player ID should be preserved
        assertEquals(playerId, result[0].playerId)
    }

    @Test
    fun testCalculateRanking_UsernamePreserved() {
        // Given: Player with specific username
        val username = "CoolPlayer"
        val player = Player("1", username)
        val players = listOf(Pair(player, 100))

        // When: Calculate ranking
        val result = calculateRanking(players)

        // Then: Username should be preserved
        assertEquals(username, result[0].username)
    }

    @Test
    fun testCalculateRanking_ScorePreserved() {
        // Given: Player with specific score
        val score = 12345
        val player = Player("1", "Player1")
        val players = listOf(Pair(player, score))

        // When: Calculate ranking
        val result = calculateRanking(players)

        // Then: Score should be preserved
        assertEquals(score, result[0].score)
    }

    // ==================== Large Dataset Tests ====================

    @Test
    fun testCalculateRanking_LargeDataset() {
        // Given: Large list of players with different scores
        val players = (1..1000).map { index ->
            Pair(
                Player("player-$index", "Player$index"),
                1000 - index
            )
        }

        // When: Calculate ranking
        val result = calculateRanking(players)

        // Then: Verify result size and correct ranking
        assertEquals(1000, result.size)
        // First player should have highest score and rank 1
        assertEquals(1, result[0].rank)
        assertEquals(999, result[0].score)
        // Last player should have lowest score and rank 1000
        assertEquals(1000, result[999].rank)
        assertEquals(0, result[999].score)
    }

    @Test
    fun testCalculateRanking_LargeDatasetWithTies() {
        // Given: Large list with many tied scores
        val players = mutableListOf<Pair<Player, Int>>()
        var playerCount = 0

        // Create groups of 5 players with same score
        for (score in 1000 downTo 0 step 100) {
            for (i in 0..4) {
                players.add(
                    Pair(
                        Player("player-${playerCount++}", "Player$playerCount"),
                        score
                    )
                )
            }
        }

        // When: Calculate ranking
        val result = calculateRanking(players)

        // Then: Verify correct rank assignment
        assertEquals(playerCount, result.size)
        // Check that ranks increment correctly (1, 1, 1, 1, 1, 6, 6, 6, 6, 6, ...)
        var expectedRank = 1
        var previousScore = result[0].score

        for (item in result) {
            if (item.score != previousScore) {
                expectedRank += 5 // 5 players had the previous score
                previousScore = item.score
            }
            assertEquals("Rank should be $expectedRank for score ${item.score}", expectedRank, item.rank)
        }
    }

    // ==================== Edge Case Tests ====================

    @Test
    fun testCalculateRanking_ZeroScore() {
        // Given: Player with zero score
        val players = listOf(
            Pair(Player("1", "Alice"), 0),
            Pair(Player("2", "Bob"), 100)
        )

        // When: Calculate ranking
        val result = calculateRanking(players)

        // Then: Zero score player should be ranked correctly
        assertEquals(2, result.size)
        assertEquals(1, result[0].rank)
        assertEquals(100, result[0].score)
        assertEquals(2, result[1].rank)
        assertEquals(0, result[1].score)
    }

    @Test
    fun testCalculateRanking_NegativeScores() {
        // Given: Players with negative scores
        val players = listOf(
            Pair(Player("1", "Alice"), 50),
            Pair(Player("2", "Bob"), -10),
            Pair(Player("3", "Charlie"), 0)
        )

        // When: Calculate ranking
        val result = calculateRanking(players)

        // Then: Should be sorted correctly with negative scores
        assertEquals(3, result.size)
        assertEquals(1, result[0].rank)
        assertEquals(50, result[0].score)
        assertEquals(2, result[1].rank)
        assertEquals(0, result[1].score)
        assertEquals(3, result[2].rank)
        assertEquals(-10, result[2].score)
    }

    @Test
    fun testCalculateRanking_HighScores() {
        // Given: Players with very high scores
        val players = listOf(
            Pair(Player("1", "Alice"), Int.MAX_VALUE),
            Pair(Player("2", "Bob"), Int.MAX_VALUE - 1),
            Pair(Player("3", "Charlie"), Int.MIN_VALUE)
        )

        // When: Calculate ranking
        val result = calculateRanking(players)

        // Then: Should handle extreme values correctly
        assertEquals(3, result.size)
        assertEquals(1, result[0].rank)
        assertEquals(Int.MAX_VALUE, result[0].score)
        assertEquals(2, result[1].rank)
        assertEquals(Int.MAX_VALUE - 1, result[1].score)
        assertEquals(3, result[2].rank)
        assertEquals(Int.MIN_VALUE, result[2].score)
    }

    // ==================== Return Type Tests ====================

    @Test
    fun testCalculateRanking_ReturnTypeIsLeaderboardItem() {
        // Given: Valid player data
        val player = Player("1", "Alice")
        val players = listOf(Pair(player, 100))

        // When: Calculate ranking
        val result = calculateRanking(players)

        // Then: Result should be LeaderboardItem type
        assertEquals(1, result.size)
        val item = result[0]
        assert(item is LeaderboardItem)
        assertEquals("1", item.playerId)
        assertEquals("Alice", item.username)
        assertEquals(100, item.score)
        assertEquals(1, item.rank)
    }

    @Test
    fun testCalculateRanking_ComplexScenario() {
        // Given: Complex real-world scenario with mixed data
        val players = listOf(
            Pair(Player("id-1", "Alice"), 500),
            Pair(Player("id-2", "Bob"), 500),
            Pair(Player("id-3", "Charlie"), 400),
            Pair(Player("id-4", "Diana"), 400),
            Pair(Player("id-5", "Eve"), 400),
            Pair(Player("id-6", "Frank"), 300),
            Pair(Player("id-7", "Grace"), 100),
            Pair(Player("id-8", "Henry"), 0),
            Pair(Player("id-9", "Ivy"), -50)
        )

        // When: Calculate ranking
        val result = calculateRanking(players)

        // Then: Verify complete ranking structure
        assertEquals(9, result.size)

        // Rank 1 (2 players with 500)
        assertEquals(1, result[0].rank)
        assertEquals(500, result[0].score)
        assertEquals(1, result[1].rank)
        assertEquals(500, result[1].score)

        // Rank 3 (3 players with 400)
        assertEquals(3, result[2].rank)
        assertEquals(400, result[2].score)
        assertEquals(3, result[3].rank)
        assertEquals(400, result[3].score)
        assertEquals(3, result[4].rank)
        assertEquals(400, result[4].score)

        // Rank 6 onwards
        assertEquals(6, result[5].rank)
        assertEquals(300, result[5].score)
        assertEquals(7, result[6].rank)
        assertEquals(100, result[6].score)
        assertEquals(8, result[7].rank)
        assertEquals(0, result[7].score)
        assertEquals(9, result[8].rank)
        assertEquals(-50, result[8].score)
    }
}

