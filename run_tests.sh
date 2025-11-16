# This helps run all the ta's test files in the folder typechecking_tests

TESTDIR="${1:-.}"

echo "=========================================="
echo "Project 3 - Semantic Analysis Test Runner"
echo "=========================================="
echo ""

# Check if directory exists
if [ ! -d "$TESTDIR" ]; then
    echo "Error: Directory '$TESTDIR' not found"
    exit 1
fi

# Find all .c files
TEST_FILES=$(find "$TESTDIR" -maxdepth 1 -name "*.c" | sort)

if [ -z "$TEST_FILES" ]; then
    echo "No test files found in $TESTDIR"
    exit 1
fi

TOTAL=0
PASSED=0
FAILED=0

echo "Running tests from: $TESTDIR"
echo ""

# Run each test
for testfile in $TEST_FILES; do
    TOTAL=$((TOTAL + 1))
    basename=$(basename "$testfile")
    
    echo "----------------------------------------"
    echo "Test: $basename"
    echo "----------------------------------------"
    
    # Run the semantic analyzer
    OUTPUT=$(java Semant.Main "$testfile" 2>&1)
    EXIT_CODE=$?
    
    # Check if it passed or failed
    if echo "$OUTPUT" | grep -q "completed successfully"; then
        echo "✓ PASS - Type checking passed"
        PASSED=$((PASSED + 1))
    elif echo "$OUTPUT" | grep -q "failed with errors"; then
        echo "✗ FAIL - Type checking detected errors:"
        echo "$OUTPUT" | grep "error" | head -5
        FAILED=$((FAILED + 1))
    else
        echo "? ERROR - Unexpected output or crash"
        echo "$OUTPUT" | head -10
        FAILED=$((FAILED + 1))
    fi
    echo ""
done

echo "=========================================="
echo "Test Summary"
echo "=========================================="
echo "Total:  $TOTAL"
echo "Passed: $PASSED"
echo "Failed: $FAILED"
echo ""

if [ $FAILED -eq 0 ]; then
    echo "✓ All tests passed!"
    exit 0
else
    echo "✗ Some tests failed"
    exit 1
fi
