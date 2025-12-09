package com.example.tags;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * Custom Test Tag Annotations - LEVEL 9
 * 
 * ╔═══════════════════════════════════════════════════════════════╗
 * ║                 COMPOSED TAG ANNOTATIONS                      ║
 * ╠═══════════════════════════════════════════════════════════════╣
 * ║  These custom annotations combine @Tag with other settings    ║
 * ║  for cleaner, more readable test code.                        ║
 * ║                                                               ║
 * ║  Benefits:                                                    ║
 * ║  • Reduce boilerplate (single annotation vs multiple)         ║
 * ║  • Enforce standards (consistent timeouts)                    ║
 * ║  • Self-documenting (clear test categories)                   ║
 * ║  • Easy to filter (mvn test -Dgroups=unit)                    ║
 * ╚═══════════════════════════════════════════════════════════════╝
 * 
 * Usage:
 *   @UnitTest
 *   void myTest() { }
 *   
 *   // Is equivalent to:
 *   @Test
 *   @Tag("unit")
 *   @Tag("fast")
 *   @Timeout(value = 1, unit = TimeUnit.SECONDS)
 *   void myTest() { }
 * 
 * @author NPCI Training Team
 * @version 1.0
 */
public class TestTags {

    // ═══════════════════════════════════════════════════════════
    // CORE TEST TYPE TAGS
    // ═══════════════════════════════════════════════════════════

    /**
     * Unit Test - Fast, isolated test with no external dependencies.
     * 
     * Characteristics:
     * - Runs in < 1 second
     * - No database, network, or file system access
     * - Uses mocks for dependencies
     * 
     * Run with: mvn test -Dgroups=unit
     */
    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Test
    @Tag("unit")
    @Tag("fast")
    @Timeout(value = 1, unit = TimeUnit.SECONDS)
    public @interface UnitTest {
    }

    /**
     * Integration Test - Tests real component interactions.
     * 
     * Characteristics:
     * - May use real database/repository
     * - Tests multiple components together
     * - Runs in < 5 seconds
     * 
     * Run with: mvn test -Dgroups=integration
     */
    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Test
    @Tag("integration")
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    public @interface IntegrationTest {
    }

    /**
     * API Test - Tests REST API endpoints.
     * 
     * Characteristics:
     * - Requires running server (or MockMvc)
     * - Tests HTTP request/response
     * - Runs in < 3 seconds per request
     * 
     * Run with: mvn test -Dgroups=api
     */
    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Test
    @Tag("api")
    @Tag("integration")
    @Timeout(value = 3, unit = TimeUnit.SECONDS)
    public @interface ApiTest {
    }

    /**
     * Performance Test - Verifies SLA compliance.
     * 
     * Characteristics:
     * - Measures execution time
     * - May run multiple iterations
     * - Longer timeout allowed
     * 
     * Run with: mvn test -Dgroups=performance
     */
    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Test
    @Tag("performance")
    @Tag("slow")
    @Timeout(value = 30, unit = TimeUnit.SECONDS)
    public @interface PerformanceTest {
    }

    /**
     * Slow Test - Test that takes longer than typical.
     * 
     * Characteristics:
     * - > 5 seconds execution time
     * - May involve large data sets
     * - Skip during quick local builds
     * 
     * Run with: mvn test -Dgroups=slow
     * Exclude with: mvn test -DexcludedGroups=slow
     */
    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Test
    @Tag("slow")
    @Timeout(value = 60, unit = TimeUnit.SECONDS)
    public @interface SlowTest {
    }

    // ═══════════════════════════════════════════════════════════
    // BANKING DOMAIN TAGS
    // ═══════════════════════════════════════════════════════════

    /**
     * P2P Transfer Test - Person-to-person UPI transfer tests.
     * 
     * Run with: mvn test -Dgroups=p2p
     */
    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Test
    @Tag("banking")
    @Tag("transfer")
    @Tag("p2p")
    public @interface P2PTransferTest {
    }

    /**
     * P2M Payment Test - Person-to-merchant payment tests.
     * 
     * Run with: mvn test -Dgroups=p2m
     */
    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Test
    @Tag("banking")
    @Tag("transfer")
    @Tag("p2m")
    public @interface P2MPaymentTest {
    }

    /**
     * Balance Test - Balance inquiry and check tests.
     * 
     * Run with: mvn test -Dgroups=balance
     */
    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Test
    @Tag("banking")
    @Tag("balance")
    @Timeout(value = 200, unit = TimeUnit.MILLISECONDS)
    public @interface BalanceTest {
    }

    /**
     * Account Test - Account creation, validation, management.
     * 
     * Run with: mvn test -Dgroups=account
     */
    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Test
    @Tag("banking")
    @Tag("account")
    public @interface AccountTest {
    }

    /**
     * RTGS Test - Real-Time Gross Settlement tests (high value).
     * 
     * Run with: mvn test -Dgroups=rtgs
     */
    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Test
    @Tag("banking")
    @Tag("transfer")
    @Tag("rtgs")
    @Tag("high-value")
    public @interface RTGSTest {
    }

    /**
     * NEFT Test - National Electronic Funds Transfer tests.
     * 
     * Run with: mvn test -Dgroups=neft
     */
    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Test
    @Tag("banking")
    @Tag("transfer")
    @Tag("neft")
    public @interface NEFTTest {
    }

    // ═══════════════════════════════════════════════════════════
    // EXECUTION ENVIRONMENT TAGS
    // ═══════════════════════════════════════════════════════════

    /**
     * Smoke Test - Critical path verification.
     * 
     * Characteristics:
     * - Quick sanity checks
     * - Run before every deployment
     * - Must all pass for system to be healthy
     * 
     * Run with: mvn test -Dgroups=smoke
     */
    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Test
    @Tag("smoke")
    @Tag("critical")
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    public @interface SmokeTest {
    }

    /**
     * Regression Test - Tests for previously fixed bugs.
     * 
     * Run with: mvn test -Dgroups=regression
     */
    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Test
    @Tag("regression")
    public @interface RegressionTest {
    }

    /**
     * Security Test - Security validation tests.
     * 
     * Run with: mvn test -Dgroups=security
     */
    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Test
    @Tag("security")
    public @interface SecurityTest {
    }

    /**
     * Manual Test - Requires human intervention.
     * 
     * Characteristics:
     * - Not run in CI/CD
     * - Needs manual setup or verification
     * 
     * Exclude with: mvn test -DexcludedGroups=manual
     */
    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Test
    @Tag("manual")
    public @interface ManualTest {
    }

    // ═══════════════════════════════════════════════════════════
    // PRIORITY TAGS
    // ═══════════════════════════════════════════════════════════

    /**
     * Critical Test - Must never fail in production.
     * 
     * Run with: mvn test -Dgroups=critical
     */
    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Test
    @Tag("critical")
    @Tag("priority-high")
    public @interface CriticalTest {
    }

    /**
     * Edge Case Test - Tests boundary conditions.
     * 
     * Run with: mvn test -Dgroups=edge-case
     */
    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Test
    @Tag("edge-case")
    @Tag("boundary")
    public @interface EdgeCaseTest {
    }

    // Private constructor - utility class
    private TestTags() {
        throw new AssertionError("Utility class - do not instantiate");
    }
}
