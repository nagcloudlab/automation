package com.example.conditional;

import com.example.model.Account;
import com.example.repository.InMemoryAccountRepository;
import com.example.service.UPITransferService;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Conditional Annotations Demo - LEVEL 7
 * 
 * ╔═══════════════════════════════════════════════════════════════╗
 * ║                 CONDITIONAL ANNOTATIONS DEMO                  ║
 * ╠═══════════════════════════════════════════════════════════════╣
 * ║  JUnit 5 provides declarative annotations for:                ║
 * ║                                                               ║
 * ║  @EnabledOnOs / @DisabledOnOs    - OS-based conditions        ║
 * ║  @EnabledOnJre / @DisabledOnJre  - Java version conditions    ║
 * ║  @EnabledIfEnvironmentVariable   - Env var conditions         ║
 * ║  @EnabledIfSystemProperty        - System property conditions ║
 * ║  @EnabledIf / @DisabledIf        - Custom method conditions   ║
 * ╚═══════════════════════════════════════════════════════════════╝
 * 
 * @author NPCI Training Team
 * @version 1.0
 */
@DisplayName("Conditional Annotations Demo")
class ConditionalAnnotationsTest {

    private InMemoryAccountRepository repository;
    private UPITransferService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryAccountRepository();
        service = new UPITransferService(repository);
        repository.addAccount(new Account("ACC001", "Rajesh", 50000.0));
        repository.addAccount(new Account("ACC002", "Priya", 25000.0));
    }

    // ═══════════════════════════════════════════════════════════
    // @EnabledOnOs / @DisabledOnOs
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Operating System Conditions")
    class OsConditionTests {

        @Test
        @EnabledOnOs(OS.LINUX)
        @DisplayName("Runs only on Linux")
        void testOnLinuxOnly() {
            System.out.println("Running on Linux!");
            // NPCI production servers typically run on Linux
            assertTrue(System.getProperty("os.name").toLowerCase().contains("linux"));
        }

        @Test
        @EnabledOnOs(OS.WINDOWS)
        @DisplayName("Runs only on Windows")
        void testOnWindowsOnly() {
            System.out.println("Running on Windows!");
            assertTrue(System.getProperty("os.name").toLowerCase().contains("windows"));
        }

        @Test
        @EnabledOnOs(OS.MAC)
        @DisplayName("Runs only on macOS")
        void testOnMacOnly() {
            System.out.println("Running on macOS!");
            assertTrue(System.getProperty("os.name").toLowerCase().contains("mac"));
        }

        @Test
        @EnabledOnOs({OS.LINUX, OS.MAC})
        @DisplayName("Runs on Linux or macOS (Unix-like)")
        void testOnUnixLike() {
            System.out.println("Running on Unix-like OS!");
            // Unix-specific tests (file permissions, etc.)
            assertNotNull(System.getProperty("user.home"));
        }

        @Test
        @DisabledOnOs(OS.WINDOWS)
        @DisplayName("Skipped on Windows")
        void testNotOnWindows() {
            System.out.println("Not running on Windows!");
            // Unix-specific file permission tests
            assertFalse(System.getProperty("os.name").toLowerCase().contains("windows"));
        }

        @Test
        @EnabledOnOs(value = OS.LINUX, architectures = "amd64")
        @DisplayName("Runs only on Linux x86-64")
        void testOnLinuxAmd64() {
            System.out.println("Running on Linux AMD64!");
            assertEquals("amd64", System.getProperty("os.arch"));
        }
    }

    // ═══════════════════════════════════════════════════════════
    // @EnabledOnJre / @DisabledOnJre
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Java Runtime Conditions")
    class JreConditionTests {

        @Test
        @EnabledOnJre(JRE.JAVA_17)
        @DisplayName("Runs only on Java 17")
        void testOnJava17() {
            System.out.println("Running on Java 17!");
            String version = System.getProperty("java.version");
            assertTrue(version.startsWith("17"));
        }

        @Test
        @EnabledOnJre({JRE.JAVA_11, JRE.JAVA_17, JRE.JAVA_21})
        @DisplayName("Runs on LTS versions (11, 17, 21)")
        void testOnLTSVersions() {
            System.out.println("Running on LTS Java version!");
            // LTS-specific tests
            assertNotNull(System.getProperty("java.version"));
        }

        @Test
        @DisabledOnJre(JRE.JAVA_8)
        @DisplayName("Skipped on Java 8")
        void testNotOnJava8() {
            System.out.println("Not running on Java 8!");
            // Uses features not in Java 8
            String version = System.getProperty("java.version");
            assertFalse(version.startsWith("1.8"));
        }

        @Test
        @EnabledForJreRange(min = JRE.JAVA_11)
        @DisplayName("Runs on Java 11 and above")
        void testOnJava11Plus() {
            System.out.println("Running on Java 11+!");
            // Modern Java features
            var message = "Using var keyword from Java 10+";
            assertNotNull(message);
        }

        @Test
        @EnabledForJreRange(min = JRE.JAVA_11, max = JRE.JAVA_17)
        @DisplayName("Runs on Java 11 through 17")
        void testOnJava11To17() {
            System.out.println("Running on Java 11-17!");
            assertNotNull(System.getProperty("java.version"));
        }
    }

    // ═══════════════════════════════════════════════════════════
    // @EnabledIfEnvironmentVariable / @DisabledIfEnvironmentVariable
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Environment Variable Conditions")
    class EnvVarConditionTests {

        @Test
        @EnabledIfEnvironmentVariable(named = "CI", matches = "true")
        @DisplayName("Runs only on CI")
        void testOnCIOnly() {
            System.out.println("Running on CI server!");
            // Extended test suite for CI
            assertEquals("true", System.getenv("CI"));
        }

        @Test
        @DisabledIfEnvironmentVariable(named = "CI", matches = "true")
        @DisplayName("Skipped on CI")
        void testNotOnCI() {
            System.out.println("Running locally (not CI)!");
            // Interactive tests that need manual verification
        }

        @Test
        @EnabledIfEnvironmentVariable(named = "ENV", matches = "PROD")
        @DisplayName("Runs only in production")
        void testInProductionOnly() {
            System.out.println("Running in PRODUCTION!");
            // Production-specific verification
        }

        @Test
        @EnabledIfEnvironmentVariable(named = "ENV", matches = "DEV|LOCAL|TEST")
        @DisplayName("Runs in non-production environments")
        void testInNonProduction() {
            System.out.println("Running in non-production!");
            // Development/test specific code
        }

        @Test
        @EnabledIfEnvironmentVariable(named = "USER", matches = "(?i).*admin.*")
        @DisplayName("Runs for admin users (case insensitive)")
        void testForAdminUsers() {
            System.out.println("Running for admin user!");
            String user = System.getenv("USER");
            assertTrue(user.toLowerCase().contains("admin"));
        }

        @Test
        @DisabledIfEnvironmentVariable(named = "SKIP_SLOW_TESTS", matches = "true")
        @DisplayName("Skipped if SKIP_SLOW_TESTS=true")
        void slowTest() {
            System.out.println("Running slow test...");
            // Simulate slow operation
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            assertTrue(true);
        }
    }

    // ═══════════════════════════════════════════════════════════
    // @EnabledIfSystemProperty / @DisabledIfSystemProperty
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("System Property Conditions")
    class SystemPropertyConditionTests {

        @Test
        @EnabledIfSystemProperty(named = "test.environment", matches = "integration")
        @DisplayName("Runs with -Dtest.environment=integration")
        void integrationTest() {
            System.out.println("Running integration test!");
            // Run with: mvn test -Dtest.environment=integration
        }

        @Test
        @EnabledIfSystemProperty(named = "java.vendor", matches = ".*Oracle.*")
        @DisplayName("Runs on Oracle JDK")
        void testOnOracleJDK() {
            System.out.println("Running on Oracle JDK!");
            assertTrue(System.getProperty("java.vendor").contains("Oracle"));
        }

        @Test
        @EnabledIfSystemProperty(named = "os.arch", matches = "amd64|x86_64")
        @DisplayName("Runs on 64-bit architecture")
        void testOn64Bit() {
            System.out.println("Running on 64-bit!");
            String arch = System.getProperty("os.arch");
            assertTrue(arch.contains("64") || arch.equals("amd64"));
        }

        @Test
        @DisabledIfSystemProperty(named = "test.skip.slow", matches = "true")
        @DisplayName("Skipped if -Dtest.skip.slow=true")
        void potentiallySlowTest() {
            System.out.println("Running potentially slow test...");
            // This test can be skipped with: mvn test -Dtest.skip.slow=true
        }

        @Test
        @EnabledIfSystemProperty(named = "test.feature.flag", matches = "enabled")
        @DisplayName("Runs when feature flag enabled")
        void featureFlagTest() {
            System.out.println("Feature flag enabled - running test!");
            // Run with: mvn test -Dtest.feature.flag=enabled
        }
    }

    // ═══════════════════════════════════════════════════════════
    // @EnabledIf / @DisabledIf - CUSTOM METHOD CONDITIONS
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Custom Method Conditions")
    class CustomConditionTests {

        @Test
        @EnabledIf("isWeekday")
        @DisplayName("Runs only on weekdays")
        void testOnWeekdays() {
            System.out.println("Running on a weekday!");
            DayOfWeek day = LocalDate.now().getDayOfWeek();
            assertTrue(day.getValue() >= 1 && day.getValue() <= 5);
        }

        @Test
        @DisabledIf("isWeekend")
        @DisplayName("Skipped on weekends")
        void testNotOnWeekends() {
            System.out.println("Not a weekend - test running!");
            DayOfWeek day = LocalDate.now().getDayOfWeek();
            assertFalse(day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY);
        }

        @Test
        @EnabledIf("isBusinessHours")
        @DisplayName("Runs during business hours (9 AM - 6 PM)")
        void testDuringBusinessHours() {
            System.out.println("Running during business hours!");
            LocalTime now = LocalTime.now();
            assertTrue(now.isAfter(LocalTime.of(9, 0)) 
                && now.isBefore(LocalTime.of(18, 0)));
        }

        @Test
        @DisabledIf("isMaintenanceWindow")
        @DisplayName("Skipped during maintenance (2 AM - 4 AM)")
        void testNotDuringMaintenance() {
            System.out.println("Not maintenance window - test running!");
            service.transfer("ACC001", "ACC002", 1000.0);
            assertEquals(49000.0, repository.loadAccountById("ACC001").getBalance());
        }

        @Test
        @EnabledIf("hasSufficientTestData")
        @DisplayName("Runs if sufficient test data exists")
        void testWithSufficientData() {
            System.out.println("Sufficient test data available!");
            assertTrue(repository.getAccountCount() >= 2);
        }

        // ═══════════════════════════════════════════════════════
        // CONDITION METHODS (must return boolean)
        // ═══════════════════════════════════════════════════════

        boolean isWeekday() {
            DayOfWeek day = LocalDate.now().getDayOfWeek();
            return day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY;
        }

        boolean isWeekend() {
            return !isWeekday();
        }

        boolean isBusinessHours() {
            LocalTime now = LocalTime.now();
            return now.isAfter(LocalTime.of(9, 0)) 
                && now.isBefore(LocalTime.of(18, 0));
        }

        boolean isMaintenanceWindow() {
            LocalTime now = LocalTime.now();
            return now.isAfter(LocalTime.of(2, 0)) 
                && now.isBefore(LocalTime.of(4, 0));
        }

        boolean hasSufficientTestData() {
            return repository.getAccountCount() >= 2;
        }
    }

    // ═══════════════════════════════════════════════════════════
    // COMBINED CONDITIONS
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Combined Conditions")
    class CombinedConditionTests {

        @Test
        @EnabledOnOs(OS.LINUX)
        @EnabledOnJre(JRE.JAVA_17)
        @DisplayName("Runs on Linux with Java 17")
        void testLinuxJava17() {
            System.out.println("Running on Linux with Java 17!");
        }

        @Test
        @EnabledOnOs({OS.LINUX, OS.MAC})
        @EnabledForJreRange(min = JRE.JAVA_11)
        @DisabledIfEnvironmentVariable(named = "CI", matches = "true")
        @DisplayName("Unix-like, Java 11+, not on CI")
        void testLocalUnixDevelopment() {
            System.out.println("Local Unix development environment!");
            // Complex condition: must be on Unix, Java 11+, and not CI
        }

        @Test
        @EnabledIf("isWeekdayBusinessHours")
        @DisplayName("Runs on weekday business hours only")
        void testWeekdayBusinessHours() {
            System.out.println("Weekday business hours!");
            service.transfer("ACC001", "ACC002", 5000.0);
            assertEquals(45000.0, repository.loadAccountById("ACC001").getBalance());
        }

        boolean isWeekdayBusinessHours() {
            DayOfWeek day = LocalDate.now().getDayOfWeek();
            LocalTime time = LocalTime.now();
            
            boolean isWeekday = day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY;
            boolean isBusinessHours = time.isAfter(LocalTime.of(9, 0)) 
                && time.isBefore(LocalTime.of(18, 0));
            
            return isWeekday && isBusinessHours;
        }
    }
}
