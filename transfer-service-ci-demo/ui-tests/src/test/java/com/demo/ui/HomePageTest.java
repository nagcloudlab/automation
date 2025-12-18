package com.demo.ui;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class HomePageTest extends BaseUiTest {

    @Test
    @DisplayName("Home page loads successfully")
    void homePageLoads() {

        page.navigate(baseUrl + "/");

        assertTrue(
                page.locator("text=Transfer Service").isVisible(),
                "Home page title should be visible"
        );

        assertTrue(
                page.locator("text=Transfer ₹100").isVisible(),
                "Transfer button should be visible"
        );
    }
}
