package com.example.tags.suites;

import org.junit.platform.suite.api.*;

/**
 * Test Suites - LEVEL 9
 * 
 * ╔═══════════════════════════════════════════════════════════════╗
 * ║                      TEST SUITES                              ║
 * ╠═══════════════════════════════════════════════════════════════╣
 * ║  Suites group related tests for selective execution:          ║
 * ║                                                               ║
 * ║  • UnitTestSuite      - Fast, isolated tests                  ║
 * ║  • IntegrationSuite   - Real component tests                  ║
 * ║  • SmokeSuite         - Critical path verification            ║
 * ║  • PerformanceSuite   - SLA and load tests                    ║
 * ║  • BankingSuite       - All banking-related tests             ║
 * ║  • FastCISuite        - Quick tests for every commit          ║
 * ║  • NightlySuite       - Comprehensive nightly tests           ║
 * ╚═══════════════════════════════════════════════════════════════╝
 * 
 * Run suites with:
 *   mvn test -Dtest=UnitTestSuite
 *   mvn test -Dtest=SmokeSuite
 * 
 * @author NPCI Training Team
 * @version 1.0
 */
public class TestSuites {

    // ═══════════════════════════════════════════════════════════
    // UNIT TEST SUITE
    // ═══════════════════════════════════════════════════════════

    /**
     * Unit Test Suite - Fast, isolated tests.
     * 
     * Run: mvn test -Dtest=UnitTestSuite
     * 
     * Characteristics:
     * - No external dependencies
     * - Runs in < 30 seconds
     * - Run on every commit
     */
    @Suite
    @SuiteDisplayName("Unit Test Suite")
    @SelectPackages("com.example")
    @IncludeTags("unit")
    @ExcludeTags({"slow", "integration", "manual"})
    public static class UnitTestSuite {
    }

    // ═══════════════════════════════════════════════════════════
    // INTEGRATION TEST SUITE
    // ═══════════════════════════════════════════════════════════

    /**
     * Integration Test Suite - Real component interaction tests.
     * 
     * Run: mvn test -Dtest=IntegrationTestSuite
     * 
     * Characteristics:
     * - Uses real repositories
     * - Tests component interactions
     * - Run on PR merge
     */
    @Suite
    @SuiteDisplayName("Integration Test Suite")
    @SelectPackages("com.example")
    @IncludeTags("integration")
    @ExcludeTags({"manual", "nightly"})
    public static class IntegrationTestSuite {
    }

    // ═══════════════════════════════════════════════════════════
    // SMOKE TEST SUITE
    // ═══════════════════════════════════════════════════════════

    /**
     * Smoke Test Suite - Critical path verification.
     * 
     * Run: mvn test -Dtest=SmokeSuite
     * 
     * Characteristics:
     * - Quick sanity checks
     * - Must all pass for deployment
     * - Run before every deployment
     */
    @Suite
    @SuiteDisplayName("Smoke Test Suite")
    @SelectPackages("com.example")
    @IncludeTags("smoke")
    public static class SmokeSuite {
    }

    // ═══════════════════════════════════════════════════════════
    // PERFORMANCE TEST SUITE
    // ═══════════════════════════════════════════════════════════

    /**
     * Performance Test Suite - SLA and load tests.
     * 
     * Run: mvn test -Dtest=PerformanceSuite
     * 
     * Characteristics:
     * - Verifies SLA compliance
     * - May take several minutes
     * - Run nightly or on-demand
     */
    @Suite
    @SuiteDisplayName("Performance Test Suite")
    @SelectPackages("com.example")
    @IncludeTags("performance")
    public static class PerformanceSuite {
    }

    // ═══════════════════════════════════════════════════════════
    // BANKING DOMAIN SUITE
    // ═══════════════════════════════════════════════════════════

    /**
     * Banking Test Suite - All banking-related tests.
     * 
     * Run: mvn test -Dtest=BankingSuite
     * 
     * Includes:
     * - Transfer tests (P2P, P2M, RTGS, NEFT)
     * - Balance tests
     * - Account tests
     */
    @Suite
    @SuiteDisplayName("Banking Domain Test Suite")
    @SelectPackages("com.example")
    @IncludeTags("banking")
    @ExcludeTags("manual")
    public static class BankingSuite {
    }

    // ═══════════════════════════════════════════════════════════
    // TRANSFER TEST SUITE
    // ═══════════════════════════════════════════════════════════

    /**
     * Transfer Test Suite - All transfer-related tests.
     * 
     * Run: mvn test -Dtest=TransferSuite
     */
    @Suite
    @SuiteDisplayName("Transfer Test Suite")
    @SelectPackages("com.example")
    @IncludeTags("transfer")
    public static class TransferSuite {
    }

    // ═══════════════════════════════════════════════════════════
    // FAST CI SUITE
    // ═══════════════════════════════════════════════════════════

    /**
     * Fast CI Suite - Quick tests for every commit.
     * 
     * Run: mvn test -Dtest=FastCISuite
     * 
     * Characteristics:
     * - Completes in < 1 minute
     * - Run on every push
     * - Excludes slow/integration tests
     */
    @Suite
    @SuiteDisplayName("Fast CI Suite")
    @SelectPackages("com.example")
    @IncludeTags({"unit", "fast", "smoke"})
    @ExcludeTags({"slow", "integration", "performance", "manual", "nightly"})
    public static class FastCISuite {
    }

    // ═══════════════════════════════════════════════════════════
    // NIGHTLY SUITE
    // ═══════════════════════════════════════════════════════════

    /**
     * Nightly Test Suite - Comprehensive testing.
     * 
     * Run: mvn test -Dtest=NightlySuite
     * 
     * Characteristics:
     * - All automated tests
     * - May take hours
     * - Run once per day
     */
    @Suite
    @SuiteDisplayName("Nightly Test Suite")
    @SelectPackages("com.example")
    @ExcludeTags("manual")  // Only exclude manual tests
    public static class NightlySuite {
    }

    // ═══════════════════════════════════════════════════════════
    // CRITICAL TEST SUITE
    // ═══════════════════════════════════════════════════════════

    /**
     * Critical Test Suite - Must-pass tests.
     * 
     * Run: mvn test -Dtest=CriticalSuite
     * 
     * Characteristics:
     * - Tests that must never fail
     * - Run before production deployment
     */
    @Suite
    @SuiteDisplayName("Critical Test Suite")
    @SelectPackages("com.example")
    @IncludeTags("critical")
    public static class CriticalSuite {
    }

    // ═══════════════════════════════════════════════════════════
    // REGRESSION SUITE
    // ═══════════════════════════════════════════════════════════

    /**
     * Regression Test Suite - Previously fixed bugs.
     * 
     * Run: mvn test -Dtest=RegressionSuite
     */
    @Suite
    @SuiteDisplayName("Regression Test Suite")
    @SelectPackages("com.example")
    @IncludeTags("regression")
    public static class RegressionSuite {
    }

    // ═══════════════════════════════════════════════════════════
    // SECURITY SUITE
    // ═══════════════════════════════════════════════════════════

    /**
     * Security Test Suite - Security validation tests.
     * 
     * Run: mvn test -Dtest=SecuritySuite
     */
    @Suite
    @SuiteDisplayName("Security Test Suite")
    @SelectPackages("com.example")
    @IncludeTags("security")
    public static class SecuritySuite {
    }

    // ═══════════════════════════════════════════════════════════
    // EDGE CASE SUITE
    // ═══════════════════════════════════════════════════════════

    /**
     * Edge Case Test Suite - Boundary condition tests.
     * 
     * Run: mvn test -Dtest=EdgeCaseSuite
     */
    @Suite
    @SuiteDisplayName("Edge Case Test Suite")
    @SelectPackages("com.example")
    @IncludeTags({"edge-case", "boundary"})
    public static class EdgeCaseSuite {
    }

    // Private constructor
    private TestSuites() {
    }
}
