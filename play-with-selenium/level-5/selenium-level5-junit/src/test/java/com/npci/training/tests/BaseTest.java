package com.npci.training.tests;

import com.npci.training.utils.TestUtils;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * BaseTest - Parent class for all Test classes using JUnit 5
 * 
 * Provides WebDriver instance to all tests
 * Handles screenshot on failure
 * Uses JUnit 5 lifecycle annotations
 */
public class BaseTest {
    
    protected WebDriver driver;
    
    @BeforeAll
    public static void setupSuite() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║   NPCI SELENIUM TEST SUITE STARTING   ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        // Setup WebDriver once for entire suite
        WebDriverManager.chromedriver().setup();
    }
    
    @BeforeEach
    public void setUp(TestInfo testInfo) {
        // Create new browser for each test
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        
        System.out.println("\n✓ Browser started for: " + testInfo.getDisplayName());
    }
    
    @AfterEach
    public void tearDown(TestInfo testInfo) {
        // Close browser
        if (driver != null) {
            driver.quit();
            System.out.println("✓ Browser closed for: " + testInfo.getDisplayName());
        }
    }
    
    @AfterAll
    public static void teardownSuite() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║   NPCI SELENIUM TEST SUITE COMPLETED  ║");
        System.out.println("╚════════════════════════════════════════╝\n");
    }
}
