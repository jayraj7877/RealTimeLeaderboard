# Ranking Logic Unit Tests - Quick Reference Guide

## 📁 Test Files Location

```
leaderboard-domain/src/test/java/com/jayant/leaderboard_domain/ranking/
├── CalculateRankingTest.kt              (50+ core tests)
├── RankingAdvancedTest.kt               (30+ advanced tests)
└── test/
    └── RankingTestUtils.kt              (test utilities)
```

## 🚀 Quick Start

### Run All Tests
```bash
./gradlew :leaderboard-domain:test
```

### Run with Report
```bash
./gradlew :leaderboard-domain:test && open leaderboard-domain/build/reports/tests/test/index.html
```

## 📊 Test Coverage Overview

### Core Tests (CalculateRankingTest)

| Category | Tests | Examples |
|----------|-------|----------|
| Basic Functionality | 3 | Empty list, Single player, Two players |
| Sorting | 1 | Unsorted input |
| Tie Handling ⭐ | 3 | Same scores, Multiple ties, All same |
| Data Integrity | 3 | ID, Username, Score preservation |
| Large Datasets | 2 | 1000+ players, With ties |
| Edge Cases | 3 | Zero, Negative, Max/Min Int |
| Return Type | 2 | Data type, Complex scenario |

### Advanced Tests (RankingAdvancedTest)

| Category | Tests | Purpose |
|----------|-------|---------|
| Performance | 1 | 10,000 players benchmark |
| Rank Sequences | 2 | Correct progression (1,1,1,4,5) |
| Stability | 1 | Consistent results |
| Boundaries | 2 | Int.MAX_VALUE, Int.MIN_VALUE |
| Consistency | 1 | Score/rank order alignment |
| Validation | 2 | Valid ranks, Range check |
| Immutability | 1 | Input not modified |
| Null Safety | 1 | No null outputs |
| Order | 1 | Descending scores |
| Size | 1 | Output matches input |
| Uniqueness | 2 | Player IDs, Usernames |

## 🔑 Key Test Scenarios

### Tie Handling (Most Important)
```kotlin
Players:  A(100), B(100), C(80), D(80), E(80)
Ranks:     1      1      3     3      3
```
✅ Players with same score get same rank  
✅ Subsequent ranks skip appropriately  
✅ Correct formula: rank = index + 1 (when score changes)

### Sorting
✅ Descending order by score  
✅ Handles unsorted input  
✅ Stable sorting

### Data Preservation
✅ Player IDs preserved  
✅ Usernames preserved  
✅ Scores preserved

### Edge Cases
✅ Empty lists  
✅ Single player  
✅ Zero and negative scores  
✅ Int.MAX_VALUE and Int.MIN_VALUE  
✅ All players with same score

## 📝 Test Utilities

### Creating Test Data

```kotlin
// Sequential scores
val players = RankingTestUtils.createPlayersWithSequentialScores(5)
// Players: Score 4, 3, 2, 1, 0

// Same scores (ties)
val tied = RankingTestUtils.createPlayersWithSameScore(3, 100)
// 3 players with score 100

// Grouped scores
val grouped = RankingTestUtils.createPlayersWithGroupedScores(listOf(2, 3, 1))
// 2 players @ 1000, 3 players @ 900, 1 player @ 800

// Random scores
val random = RankingTestUtils.createPlayersWithRandomScores(100, maxScore = 1000)
// 100 players with random scores 0-1000
```

### Validation Utilities

```kotlin
// Validate complete ranking
RankingTestUtils.assertValidRanking(result, "Custom error message")

// Check tie handling
val isValid = RankingTestUtils.verifyTieHandling(result)

// Get statistics
val stats = RankingTestUtils.getLeaderboardStats(result)
println(stats) // Pretty-printed statistics

// Format for debugging
println(RankingTestUtils.formatLeaderboard(result))
```

## 🎯 Most Important Tests

1. **testCalculateRanking_MultipleTies** - Verifies tie handling with rank skipping
2. **testCalculateRanking_UnsortedInput** - Ensures sorting is correct
3. **testCalculateRanking_ComplexScenario** - Real-world scenario with all features
4. **testCalculateRanking_LargeDatasetWithTies** - Performance and correctness
5. **RankingAdvancedTest.testCalculateRanking_RankSequenceIsCorrect** - Rank progression

## 📈 Performance Expectations

| Test Type | Expected Time |
|-----------|--------------|
| Single test | 1-50ms |
| Basic tests (10) | 50-100ms |
| All tests (80+) | <5 seconds |
| Large dataset (10,000 players) | <100ms |

## ❌ Common Issues & Fixes

### Tests Not Running
**Problem**: Tests not found by Gradle  
**Solution**: 
```bash
./gradlew :leaderboard-domain:test  # Use module path
```

### Import Errors
**Problem**: RankingTestUtils import fails  
**Solution**: Ensure file is at:
```
leaderboard-domain/src/test/java/com/jayant/leaderboard_domain/ranking/test/RankingTestUtils.kt
```

### Missing Dependencies
**Problem**: JUnit dependency not found  
**Solution**: Check `leaderboard-domain/build.gradle.kts` has:
```kotlin
testImplementation(libs.junit)
```

### Assertion Failures
**Problem**: Test fails on rank validation  
**Solution**: 
- Review the formatted leaderboard output
- Check tie handling logic
- Verify expected rank calculation

## 📚 Documentation Files

| File | Purpose |
|------|---------|
| RANKING_TESTS_README.md | Complete test documentation |
| RANKING_TESTS_IMPLEMENTATION.md | Implementation summary |
| RANKING_TESTS_QUICK_REFERENCE.md | This file |

## 🔗 Related Files

| File | Purpose |
|------|---------|
| `calculateRanking.kt` | Function being tested |
| `LeaderboardItem.kt` | Output data class |
| `Player.kt` | Input data class |

## 💡 Extending Tests

### Adding a New Test

```kotlin
@Test
fun testCalculateRanking_YourScenario() {
    // Given: Describe your test data
    val players = RankingTestUtils.createPlayersWithSameScore(3, 100)
    
    // When: What you're testing
    val result = calculateRanking(players)
    
    // Then: Expected outcome
    assertEquals(3, result.size)
    assertEquals(1, result[0].rank)
    RankingTestUtils.assertValidRanking(result)
}
```

### Test Naming Convention
```
testCalculateRanking_[Scenario]
```

Examples:
- `testCalculateRanking_EmptyList`
- `testCalculateRanking_TwoPlayersSameScore`
- `testCalculateRanking_LargeDataset`

## ✅ Validation Checklist

Before committing test changes:
- [ ] All tests pass: `./gradlew :leaderboard-domain:test`
- [ ] No compiler errors
- [ ] Test names follow convention
- [ ] Comments are clear
- [ ] New tests use AAA pattern
- [ ] Edge cases are covered

## 📞 Support

For questions about:
- **Test structure**: See RANKING_TESTS_README.md
- **How to use**: See this file
- **What was built**: See RANKING_TESTS_IMPLEMENTATION.md
- **Test code**: See test files directly

---

**Last Updated**: March 2, 2026  
**Status**: Complete & Ready for Use ✅

