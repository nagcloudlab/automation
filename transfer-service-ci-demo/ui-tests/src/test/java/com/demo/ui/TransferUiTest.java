package com.demo.ui;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TransferUiTest extends BaseUiTest {

    @Test
    @DisplayName("User can perform fund transfer from UI")
    void shouldPerformTransfer() {

        page.navigate(baseUrl + "/");

        // Click transfer button
        page.click("text=Transfer ₹100");

        // Validate response JSON is shown
        page.waitForSelector("#result");

        String resultText = page.locator("#result").innerText();

        assertTrue(resultText.contains("SUCCESS"), "Transfer should be successful");
        assertTrue(resultText.contains("transactionId"), "Transaction ID should be present");
    }
}
