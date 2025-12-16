package com.npci.training;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

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

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");   // recommended for Chrome 109+
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-gpu");

        driver = new ChromeDriver(options);
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
