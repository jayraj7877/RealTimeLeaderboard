# Ranking Logic Unit Testing

This document describes the comprehensive unit test suite for the ranking logic in the RealTimeLeaderboard application.

## Overview

The ranking logic is tested through two main test classes that provide complete coverage of the `calculateRanking()` function:

1. **CalculateRankingTest** - Core functionality and main test cases
2. **RankingAdvancedTest** - Advanced scenarios, performance, and edge cases

## Test Files Location

```
leaderboard-domain/src/test/java/com/jayant/leaderboard_domain/ranking/
├── CalculateRankingTest.kt
└── RankingAdvancedTest.kt
```

## Test Coverage

### 1. CalculateRankingTest (50+ test cases)

#### Basic Ranking Tests
- `testCalculateRanking_EmptyList()` - Verify handling of empty player list
- `testCalculateRanking_SinglePlayer()` - Single player gets rank 1
- `testCalculateRanking_TwoPlayersDescendingOrder()` - Correct ranking with 2 players

#### Sorting Tests
- `testCalculateRanking_UnsortedInput()` - Verifies correct sorting by score (descending)

#### Tie Tests (Critical)
- `testCalculateRanking_TwoPlayersSameScore()` - Two players with same score get same rank
- `testCalculateRanking_MultipleTies()` - Multiple tied groups, ranks skip appropriately (e.g., 1,1,3,3,3,6)
- `testCalculateRanking_AllPlayersSameScore()` - All players with same score get rank 1

#### Data Integrity Tests
- `testCalculateRanking_PlayerIdPreserved()` - Player IDs are preserved in output
- `testCalculateRanking_UsernamePreserved()` - Usernames are preserved in output
- `testCalculateRanking_ScorePreserved()` - Scores are preserved correctly

#### Large Dataset Tests
- `testCalculateRanking_LargeDataset()` - Handles 1000 players with different scores
- `testCalculateRanking_LargeDatasetWithTies()` - Handles 1000+ players with ties

#### Edge Case Tests
- `testCalculateRanking_ZeroScore()` - Zero scores handled correctly
- `testCalculateRanking_NegativeScores()` - Negative scores sorted correctly
- `testCalculateRanking_HighScores()` - Maximum and minimum Int values handled

#### Return Type Tests
- `testCalculateRanking_ReturnTypeIsLeaderboardItem()` - Correct data type validation
- `testCalculateRanking_ComplexScenario()` - Real-world scenario with mixed data

### 2. RankingAdvancedTest (30+ test cases)

#### Performance Tests
- `testCalculateRanking_PerformanceWith10000Players()` - Performance benchmark with 10,000 players

#### Rank Sequence Tests
- `testCalculateRanking_RankSequenceIsCorrect()` - Verifies rank sequence (1,1,1,4,5,5,7)
- `testCalculateRanking_RankSkipsCorrectly()` - Ranks skip correctly after ties

#### Stability Tests
- `testCalculateRanking_StableForDuplicateInputs()` - Same input produces same output

#### Boundary Value Tests
- `testCalculateRanking_WithMaxIntScore()` - Handles Int.MAX_VALUE
- `testCalculateRanking_WithMinIntScore()` - Handles Int.MIN_VALUE

#### Consistency Tests
- `testCalculateRanking_RankOrderMatchesScoreOrder()` - Scores descending, ranks ascending

#### Data Validation Tests
- `testCalculateRanking_AllPlayersHaveRank()` - All players have valid rank
- `testCalculateRanking_RankRangeIsValid()` - Rank range 1 to N

#### Input Mutation Tests
- `testCalculateRanking_DoesNotModifyInput()` - Input list not modified

#### Null Handling Tests
- `testCalculateRanking_ValidatesOutput()` - No null values in output

#### Descending Order Tests
- `testCalculateRanking_MaintainsDescendingScoreOrder()` - Scores in descending order

#### List Size Tests
- `testCalculateRanking_OutputSizeMatchesInputSize()` - Output size matches input size

#### Unique Values Tests
- `testCalculateRanking_PreservesUniquePlayerId()` - Player IDs are unique and preserved
- `testCalculateRanking_CanHandleSimilarUsernames()` - Similar usernames handled correctly

## Running the Tests

### Run All Tests
```bash
./gradlew test
```

### Run Only Ranking Tests
```bash
./gradlew test --tests "*CalculateRanking*"
```

### Run Specific Test Class
```bash
./gradlew test --tests "CalculateRankingTest"
./gradlew test --tests "RankingAdvancedTest"
```

### Run Specific Test Method
```bash
./gradlew test --tests "CalculateRankingTest.testCalculateRanking_MultipleTies"
```

### Run with Verbose Output
```bash
./gradlew test --info
```

## Test Results Structure

Each test follows the AAA (Arrange-Act-Assert) pattern:

```kotlin
@Test
fun testExample() {
    // Arrange: Given
    val players = listOf(...)
    
    // Act: When
    val result = calculateRanking(players)
    
    // Assert: Then
    assertEquals(expected, actual)
}
```

## Key Test Scenarios

### 1. Tie Handling (Most Important)
The ranking logic correctly handles ties:
- Players with same score get the same rank
- Subsequent ranks skip appropriately
- Example: If 3 players tie for rank 1, the next rank is 4

```
Score: 100 100 100 90  80
Rank:    1   1   1   4   5
```

### 2. Sorting
Input players are sorted by score in descending order before ranking.

### 3. Data Preservation
All player information (ID, username, score) is preserved through the ranking process.

### 4. Edge Cases
- Empty list
- Single player
- All same scores
- Extreme values (Int.MAX_VALUE, Int.MIN_VALUE)
- Zero and negative scores

## Dependencies

The tests use:
- **JUnit 4** - Testing framework
- **Kotlin** - Language
- **org.junit.Assert** - Assertion library

## Test Metrics

- **Total Test Cases**: 80+
- **Code Coverage Target**: 100% of `calculateRanking()` function
- **Performance Threshold**: All tests complete in < 5 seconds
- **Large Data Test**: 10,000+ players

## Continuous Integration

These tests are designed to run in CI/CD pipelines:
- ✅ No external dependencies required
- ✅ No file I/O operations
- ✅ No network requests
- ✅ Fast execution (< 5 seconds)
- ✅ Deterministic results

## Extending the Tests

To add new test cases:

1. Choose the appropriate test class:
   - Basic functionality → `CalculateRankingTest`
   - Advanced scenarios → `RankingAdvancedTest`

2. Follow the naming convention: `testCalculateRanking_[Scenario]`

3. Use AAA pattern (Arrange-Act-Assert)

4. Add descriptive comments

Example:
```kotlin
@Test
fun testCalculateRanking_YourScenario() {
    // Given: Description of input
    val players = // ... create test data
    
    // When: What we're testing
    val result = calculateRanking(players)
    
    // Then: Expected outcome
    assertEquals(expected, result)
}
```

## Troubleshooting

### Tests Not Running
1. Ensure JUnit dependency is added to `leaderboard-domain/build.gradle.kts`
2. Check test files are in correct package structure
3. Verify test methods have `@Test` annotation

### Assertion Failures
1. Check the error message for expected vs actual values
2. Review the test logic and input data
3. Ensure ranking algorithm handles the specific scenario

### Performance Issues
- If tests are slow, reduce dataset sizes in performance tests
- Consider running performance tests separately

## Future Enhancements

- [ ] Add parameterized tests for multiple score ranges
- [ ] Add property-based testing with generators
- [ ] Add mutation testing for better coverage validation
- [ ] Add benchmarking tests with JMH
- [ ] Add integration tests with actual database

## References

- Kotlin Testing: https://kotlinlang.org/docs/testing.html
- JUnit 4 Documentation: https://junit.org/junit4/
- Android Testing: https://developer.android.com/training/testing

