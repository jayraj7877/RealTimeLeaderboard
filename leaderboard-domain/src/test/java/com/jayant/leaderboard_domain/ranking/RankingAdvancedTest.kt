package com.jayant.leaderboard_domain.ranking

import com.jayant.score_engine.model.Player
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test


class RankingAdvancedTest {

    // ==================== Performance Tests ====================

    @Test
    fun testCalculateRanking_PerformanceWith10000Players() {
        // Given: 10,000 players with random scores
        val players = (1..10000).map { index ->
            Pair(
                Player("player-$index", "Player$index"),
                (Math.random() * 10000).toInt()
            )
        }

        // When: Calculate ranking
        val startTime = System.currentTimeMillis()
        val result = calculateRanking(players)
        val endTime = System.currentTimeMillis()

        // Then: Should complete in reasonable time (< 1 second)
        val executionTime = endTime - startTime
        assertTrue("Ranking calculation took ${executionTime}ms", executionTime < 1000)
        assertEquals(10000, result.size)
    }

    // ==================== Rank Sequence Tests ====================

    @Test
    fun testCalculateRanking_RankSequenceIsCorrect() {
        // Given: Players with specific scores
        val players = listOf(
            Pair(Player("1", "Alice"), 100),
            Pair(Player("2", "Bob"), 100),
            Pair(Player("3", "Charlie"), 100),
            Pair(Player("4", "Diana"), 90),
            Pair(Player("5", "Eve"), 80),
            Pair(Player("6", "Frank"), 80),
            Pair(Player("7", "Grace"), 70)
        )

        // When: Calculate ranking
        val result = calculateRanking(players)

        // Then: Rank sequence should be 1,1,1,4,5,5,7 (skipping 2 and 3)
        assertEquals(listOf(1, 1, 1, 4, 5, 5, 7), result.map { it.rank })
    }

    @Test
    fun testCalculateRanking_RankSkipsCorrectly() {
        // Given: Players with groups of ties
        val players = listOf(
            Pair(Player("1", "A"), 100),
            Pair(Player("2", "B"), 100),
            Pair(Player("3", "C"), 100),
            Pair(Player("4", "D"), 100),
            Pair(Player("5", "E"), 90)
        )

        // When: Calculate ranking
        val result = calculateRanking(players)

        // Then: Rank should skip to 5
        assertEquals(4, result.filter { it.rank == 1 }.size)
        assertEquals(1, result.filter { it.rank == 5 }.size)
    }

    // ==================== Stability Tests ====================

    @Test
    fun testCalculateRanking_StableForDuplicateInputs() {
        // Given: Same input called multiple times
        val players = listOf(
            Pair(Player("1", "Alice"), 100),
            Pair(Player("2", "Bob"), 50)
        )

        // When: Calculate ranking multiple times
        val result1 = calculateRanking(players)
        val result2 = calculateRanking(players)
        val result3 = calculateRanking(players)

        // Then: Results should be identical
        assertEquals(result1, result2)
        assertEquals(result2, result3)
    }

    // ==================== Boundary Value Tests ====================

    @Test
    fun testCalculateRanking_WithMaxIntScore() {
        // Given: Player with maximum Int value
        val players = listOf(
            Pair(Player("1", "Alice"), Int.MAX_VALUE)
        )

        // When: Calculate ranking
        val result = calculateRanking(players)

        // Then: Should handle correctly
        assertEquals(1, result[0].rank)
        assertEquals(Int.MAX_VALUE, result[0].score)
    }

    @Test
    fun testCalculateRanking_WithMinIntScore() {
        // Given: Player with minimum Int value
        val players = listOf(
            Pair(Player("1", "Alice"), Int.MIN_VALUE)
        )

        // When: Calculate ranking
        val result = calculateRanking(players)

        // Then: Should handle correctly
        assertEquals(1, result[0].rank)
        assertEquals(Int.MIN_VALUE, result[0].score)
    }

    // ==================== Consistency Tests ====================

    @Test
    fun testCalculateRanking_RankOrderMatchesScoreOrder() {
        // Given: Players with distinct scores
        val players = listOf(
            Pair(Player("1", "Alice"), 1000),
            Pair(Player("2", "Bob"), 500),
            Pair(Player("3", "Charlie"), 250),
            Pair(Player("4", "Diana"), 100),
            Pair(Player("5", "Eve"), 50)
        )

        // When: Calculate ranking
        val result = calculateRanking(players)

        // Then: Scores should be in descending order and ranks in ascending order
        var lastScore = Int.MAX_VALUE
        var lastRank = 0

        for (item in result) {
            assertTrue("Score should decrease: $lastScore > ${item.score}", item.score <= lastScore)
            assertTrue("Rank should increase or stay same: $lastRank <= ${item.rank}", item.rank >= lastRank)
            lastScore = item.score
            lastRank = item.rank
        }
    }

    // ==================== Data Validation Tests ====================

    @Test
    fun testCalculateRanking_AllPlayersHaveRank() {
        // Given: Various players
        val players = listOf(
            Pair(Player("1", "Alice"), 100),
            Pair(Player("2", "Bob"), 90),
            Pair(Player("3", "Charlie"), 80)
        )

        // When: Calculate ranking
        val result = calculateRanking(players)

        // Then: Every player should have a valid rank
        for (item in result) {
            assertTrue("Rank should be > 0", item.rank > 0)
            assertTrue("Rank should be <= total players", item.rank <= result.size)
        }
    }

    @Test
    fun testCalculateRanking_RankRangeIsValid() {
        // Given: 100 players with different scores
        val players = (1..100).map { index ->
            Pair(Player("id-$index", "Player$index"), 100 - index)
        }

        // When: Calculate ranking
        val result = calculateRanking(players)

        // Then: Min rank should be 1, max rank should be 100
        val minRank = result.minOf { it.rank }
        val maxRank = result.maxOf { it.rank }

        assertEquals(1, minRank)
        assertEquals(100, maxRank)
    }

    // ==================== Input Mutation Tests ====================

    @Test
    fun testCalculateRanking_DoesNotModifyInput() {
        // Given: Mutable list of players
        val players = mutableListOf(
            Pair(Player("1", "Alice"), 100),
            Pair(Player("2", "Bob"), 50)
        )
        val originalOrder = players.toList()

        // When: Calculate ranking
        calculateRanking(players)

        // Then: Input list should not be modified
        assertEquals(originalOrder, players)
    }

    // ==================== Null Handling Tests ====================

    @Test
    fun testCalculateRanking_ValidatesOutput() {
        // Given: Valid input
        val players = listOf(
            Pair(Player("1", "Alice"), 100)
        )

        // When: Calculate ranking
        val result = calculateRanking(players)

        // Then: Output should not contain null values
        assertNotNull(result)
        assertNotNull(result[0].playerId)
        assertNotNull(result[0].username)
    }

    // ==================== Descending Order Tests ====================

    @Test
    fun testCalculateRanking_MaintainsDescendingScoreOrder() {
        // Given: Random order of players
        val players = listOf(
            Pair(Player("5", "Eve"), 50),
            Pair(Player("1", "Alice"), 500),
            Pair(Player("3", "Charlie"), 300),
            Pair(Player("2", "Bob"), 200),
            Pair(Player("4", "Diana"), 100)
        )

        // When: Calculate ranking
        val result = calculateRanking(players)

        // Then: Scores should be strictly descending (ignoring ties)
        val scores = result.map { it.score }
        for (i in 0 until scores.size - 1) {
            assertTrue("Scores should be descending", scores[i] >= scores[i + 1])
        }
    }

    // ==================== List Size Tests ====================

    @Test
    fun testCalculateRanking_OutputSizeMatchesInputSize() {
        // Given: Various sizes of input
        for (size in listOf(1, 5, 10, 50, 100)) {
            val players = (1..size).map { index ->
                Pair(Player("id-$index", "Player$index"), size - index)
            }

            // When: Calculate ranking
            val result = calculateRanking(players)

            // Then: Output size should match input size
            assertEquals("Output size should match input size $size", size, result.size)
        }
    }

    // ==================== Unique Values Tests ====================

    @Test
    fun testCalculateRanking_PreservesUniquePlayerId() {
        // Given: Players with unique IDs
        val players = listOf(
            Pair(Player("unique-id-1", "Alice"), 100),
            Pair(Player("unique-id-2", "Bob"), 90),
            Pair(Player("unique-id-3", "Charlie"), 80)
        )

        // When: Calculate ranking
        val result = calculateRanking(players)

        // Then: All player IDs should be unique and preserved
        val playerIds = result.map { it.playerId }
        assertEquals(playerIds.size, playerIds.toSet().size)
    }

    @Test
    fun testCalculateRanking_CanHandleSimilarUsernames() {
        // Given: Players with similar usernames
        val players = listOf(
            Pair(Player("1", "Player"), 100),
            Pair(Player("2", "Player1"), 90),
            Pair(Player("3", "Player11"), 80),
            Pair(Player("4", "Player111"), 70)
        )

        // When: Calculate ranking
        val result = calculateRanking(players)

        // Then: All usernames should be preserved correctly
        assertEquals("Player", result[0].username)
        assertEquals("Player1", result[1].username)
        assertEquals("Player11", result[2].username)
        assertEquals("Player111", result[3].username)
    }
}

