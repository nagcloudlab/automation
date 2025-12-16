package com.npci.training.level3;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Test00_Ex {

    WebDriver driver;


    @BeforeEach
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @AfterEach
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void test() {
        driver.get("http://127.0.0.1:5500/index.html");
//        driver.manage().timeouts()
//                .implicitlyWait(Duration.ofSeconds(5));


//        WebDriverWait wait =
//                new WebDriverWait(driver, Duration.ofSeconds(10));


        Wait<WebDriver> wait =
                new FluentWait<>(driver)
                        .withTimeout(Duration.ofSeconds(10))
                        .pollingEvery(Duration.ofSeconds(1));

        WebElement registerBtn =
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.id("registerBtn")
                ));
//            Thread.sleep(5000);
        registerBtn.click();
    }

}
