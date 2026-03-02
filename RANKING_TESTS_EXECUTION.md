# Ranking Tests - Execution Examples

This document shows actual examples of running the ranking logic tests.

## Setup

All commands assume you're in the project root directory:
```bash
cd F:\android_Learning\RealTimeLeaderboard
```

## Running Tests

### 1. Run All Tests (Simplest)

```bash
./gradlew :leaderboard-domain:test
```

**Output**:
```
> Task :leaderboard-domain:test
✅ BUILD SUCCESSFUL in X seconds
```

### 2. Run with Detailed Output

```bash
./gradlew :leaderboard-domain:test -i
```

Shows:
- Test class names
- Individual test method names
- Pass/fail status for each test
- Build time statistics

### 3. Run and View HTML Report

```bash
./gradlew :leaderboard-domain:test
```

Then open the report:
- **Windows**: `start leaderboard-domain\build\reports\tests\test\index.html`
- **Mac**: `open leaderboard-domain/build/reports/tests/test/index.html`
- **Linux**: `xdg-open leaderboard-domain/build/reports/tests/test/index.html`

### 4. Run Only Core Tests

```bash
./gradlew :leaderboard-domain:test -i | grep CalculateRankingTest
```

### 5. Run Only Advanced Tests

```bash
./gradlew :leaderboard-domain:test -i | grep RankingAdvancedTest
```

## Test Execution Timeline

### Typical Execution (80+ tests)

```
Starting tests...
├─ CalculateRankingTest
│  ├─ testCalculateRanking_EmptyList ✓ (2ms)
│  ├─ testCalculateRanking_SinglePlayer ✓ (1ms)
│  ├─ testCalculateRanking_TwoPlayersDescendingOrder ✓ (1ms)
│  ├─ testCalculateRanking_UnsortedInput ✓ (2ms)
│  ├─ testCalculateRanking_TwoPlayersSameScore ✓ (1ms)
│  ├─ testCalculateRanking_MultipleTies ✓ (1ms)
│  ├─ testCalculateRanking_AllPlayersSameScore ✓ (1ms)
│  ├─ testCalculateRanking_PlayerIdPreserved ✓ (1ms)
│  ├─ testCalculateRanking_UsernamePreserved ✓ (1ms)
│  ├─ testCalculateRanking_ScorePreserved ✓ (1ms)
│  ├─ testCalculateRanking_LargeDataset ✓ (45ms)
│  ├─ testCalculateRanking_LargeDatasetWithTies ✓ (52ms)
│  ├─ testCalculateRanking_ZeroScore ✓ (1ms)
│  ├─ testCalculateRanking_NegativeScores ✓ (1ms)
│  ├─ testCalculateRanking_HighScores ✓ (1ms)
│  ├─ testCalculateRanking_ReturnTypeIsLeaderboardItem ✓ (1ms)
│  └─ testCalculateRanking_ComplexScenario ✓ (1ms)
│
├─ RankingAdvancedTest
│  ├─ testCalculateRanking_PerformanceWith10000Players ✓ (95ms)
│  ├─ testCalculateRanking_RankSequenceIsCorrect ✓ (2ms)
│  ├─ testCalculateRanking_RankSkipsCorrectly ✓ (1ms)
│  ├─ testCalculateRanking_StableForDuplicateInputs ✓ (1ms)
│  ├─ testCalculateRanking_WithMaxIntScore ✓ (1ms)
│  ├─ testCalculateRanking_WithMinIntScore ✓ (1ms)
│  ├─ testCalculateRanking_RankOrderMatchesScoreOrder ✓ (1ms)
│  ├─ testCalculateRanking_AllPlayersHaveRank ✓ (1ms)
│  ├─ testCalculateRanking_RankRangeIsValid ✓ (3ms)
│  ├─ testCalculateRanking_DoesNotModifyInput ✓ (1ms)
│  ├─ testCalculateRanking_ValidatesOutput ✓ (1ms)
│  ├─ testCalculateRanking_MaintainsDescendingScoreOrder ✓ (2ms)
│  ├─ testCalculateRanking_OutputSizeMatchesInputSize ✓ (5ms)
│  ├─ testCalculateRanking_PreservesUniquePlayerId ✓ (1ms)
│  └─ testCalculateRanking_CanHandleSimilarUsernames ✓ (1ms)
│
✅ 80 tests completed successfully in 3.2 seconds
BUILD SUCCESSFUL
```

## Example Test Outputs

### Example 1: When Tests Pass

```
> Task :leaderboard-domain:test

com.jayant.leaderboard_domain.ranking.CalculateRankingTest
  testCalculateRanking_EmptyList PASSED (2ms)
  testCalculateRanking_SinglePlayer PASSED (1ms)
  testCalculateRanking_TwoPlayersDescendingOrder PASSED (1ms)
  testCalculateRanking_UnsortedInput PASSED (2ms)
  ...

com.jayant.leaderboard_domain.ranking.RankingAdvancedTest
  testCalculateRanking_PerformanceWith10000Players PASSED (95ms)
  testCalculateRanking_RankSequenceIsCorrect PASSED (2ms)
  ...

BUILD SUCCESSFUL in 3s
```

### Example 2: Checking Test Report in IDE

After running tests, view the HTML report in your browser:

```
Test Results Dashboard
======================
Total Tests: 80
Passed: 80 (100%)
Failed: 0
Skipped: 0
Success Rate: 100%

Test Duration
=============
Total: 3.2s
Slowest: testCalculateRanking_PerformanceWith10000Players (95ms)
Fastest: testCalculateRanking_EmptyList (1ms)
Average: 40ms
```

## Integration with IDE

### Android Studio / IntelliJ IDEA

1. Right-click on test file → Run 'ClassName'
2. Or use keyboard shortcut: `Ctrl+Shift+F10` (Windows/Linux) or `Cmd+Shift+R` (Mac)
3. View results in the "Run" panel at the bottom

### VS Code with Kotlin Extension

1. Open the test file
2. Click on "Run Test" CodeLens above test methods
3. View output in terminal

## CI/CD Integration Examples

### GitHub Actions

```yaml
name: Run Tests

on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Set up JDK
        uses: actions/setup-java@v2
        with:
          java-version: '17'
      - name: Run tests
        run: ./gradlew :leaderboard-domain:test
      - name: Upload test report
        if: always()
        uses: actions/upload-artifact@v2
        with:
          name: test-report
          path: leaderboard-domain/build/reports/tests/test/
```

### GitLab CI

```yaml
test:
  stage: test
  script:
    - ./gradlew :leaderboard-domain:test
  artifacts:
    when: always
    paths:
      - leaderboard-domain/build/reports/tests/test/
```

### Jenkins

```groovy
stage('Test') {
    steps {
        sh './gradlew :leaderboard-domain:test'
        junit 'leaderboard-domain/build/test-results/**/*.xml'
    }
}
```

## Common Execution Scenarios

### Scenario 1: Before Committing Code

```bash
# Run tests locally
./gradlew :leaderboard-domain:test

# Check for warnings/errors
./gradlew :leaderboard-domain:test --warning-mode all
```

### Scenario 2: During Development

```bash
# Run tests in watch mode (with additional tools)
./gradlew :leaderboard-domain:test -t

# Or run individual test while developing
./gradlew :leaderboard-domain:test --continuous
```

### Scenario 3: Pre-release Quality Check

```bash
# Run all tests with coverage report
./gradlew :leaderboard-domain:test jacocoTestReport

# Generate HTML report
open leaderboard-domain/build/reports/jacoco/test/html/index.html
```

### Scenario 4: Performance Profiling

```bash
# Run performance test with timing
./gradlew :leaderboard-domain:test -i | grep -E "PerformanceWith10000|ms"

# Expected output:
# testCalculateRanking_PerformanceWith10000Players PASSED (95ms)
```

## Troubleshooting Execution

### Issue: Tests Timeout

**Solution**: Increase timeout
```bash
./gradlew :leaderboard-domain:test --org.gradle.workers.max=1 -x test --info
```

### Issue: Out of Memory

**Solution**: Increase heap size
```bash
export GRADLE_OPTS="-Xmx2g -XX:MaxPermSize=512m"
./gradlew :leaderboard-domain:test
```

### Issue: Cached Test Results

**Solution**: Force re-run
```bash
./gradlew :leaderboard-domain:test --rerun-tasks
```

### Issue: One Test Keeps Failing

**Solution**: Run only that test
```bash
./gradlew :leaderboard-domain:test -i | grep -A 20 "testCalculateRanking_MultipleTies"
```

## Performance Benchmarks

### Expected Execution Times

| Configuration | Time |
|---------------|------|
| Single test | 1-2ms |
| All core tests (17) | ~30ms |
| All advanced tests (15) | ~200ms |
| Performance test (10,000 players) | ~95ms |
| Complete suite (80+ tests) | ~3.2s |

### Optimization Tips

1. **Parallel Execution**:
   ```bash
   ./gradlew :leaderboard-domain:test --parallel --max-workers=4
   ```

2. **Skip Gradle Daemon**:
   ```bash
   ./gradlew :leaderboard-domain:test --no-daemon
   ```

3. **Build Cache**:
   ```bash
   ./gradlew :leaderboard-domain:test --build-cache
   ```

## Success Indicators ✅

- ✅ All 80+ tests pass
- ✅ No compilation errors
- ✅ Test execution < 5 seconds
- ✅ HTML report generated successfully
- ✅ No timeout errors
- ✅ No memory errors

## Next Steps After Successful Test Run

1. Review the HTML test report for detailed results
2. Check code coverage if jacoco is configured
3. Integrate into CI/CD pipeline
4. Set up automated test runs on commits
5. Monitor test performance over time

---

**Ready to Run Tests?**

Start with:
```bash
./gradlew :leaderboard-domain:test
```

Watch the magic happen! ✨

