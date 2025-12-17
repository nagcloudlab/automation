package com.npci.training.pages;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

/**
 * TransferPage - Page Object for Money Transfer
 */
public class TransferPage extends BasePage {
    
    // Locators
    private final Locator heading;
    private final Locator fromAccountDropdown;
    private final Locator toAccountInput;
    private final Locator amountInput;
    private final Locator remarksInput;
    private final Locator transferButton;
    private final Locator successMessage;
    private final Locator transactionId;
    private final Locator errorMessage;
    
    // Constructor
    public TransferPage(Page page) {
        super(page);
        
        // Initialize locators
        this.heading = page.getByRole(AriaRole.HEADING,
            new Page.GetByRoleOptions().setName("Transfer Money"));
        this.fromAccountDropdown = page.getByLabel("From Account");
        this.toAccountInput = page.getByLabel("To Account");
        this.amountInput = page.getByLabel("Amount");
        this.remarksInput = page.getByLabel("Remarks");
        this.transferButton = page.getByRole(AriaRole.BUTTON,
            new Page.GetByRoleOptions().setName("Transfer"));
        this.successMessage = page.getByText("Transfer successful");
        this.transactionId = page.getByTestId("transaction-id");
        this.errorMessage = page.locator(".error-message");
    }
    
    /**
     * Select from account
     * @param accountType Account type (e.g., "Savings", "Current")
     * @return this for method chaining
     */
    public TransferPage selectFromAccount(String accountType) {
        fromAccountDropdown.selectOption(accountType.toLowerCase());
        return this;
    }
    
    /**
     * Enter to account number
     * @param accountNumber Account number or UPI ID
     * @return this for method chaining
     */
    public TransferPage enterToAccount(String accountNumber) {
        toAccountInput.fill(accountNumber);
        return this;
    }
    
    /**
     * Enter amount
     * @param amount Amount to transfer
     * @return this for method chaining
     */
    public TransferPage enterAmount(String amount) {
        amountInput.fill(amount);
        return this;
    }
    
    /**
     * Enter remarks
     * @param remarks Transfer remarks
     * @return this for method chaining
     */
    public TransferPage enterRemarks(String remarks) {
        remarksInput.fill(remarks);
        return this;
    }
    
    /**
     * Click transfer button
     * @return this for method chaining
     */
    public TransferPage clickTransfer() {
        transferButton.click();
        return this;
    }
    
    /**
     * Complete transfer (all-in-one method)
     * @param fromAccount From account type
     * @param toAccount To account number
     * @param amount Amount
     * @param remarks Remarks
     * @return this for method chaining
     */
    public TransferPage doTransfer(String fromAccount, String toAccount, 
                                     String amount, String remarks) {
        selectFromAccount(fromAccount);
        enterToAccount(toAccount);
        enterAmount(amount);
        enterRemarks(remarks);
        clickTransfer();
        return this;
    }
    
    /**
     * Check if success message is displayed
     * @return true if success
     */
    public boolean isTransferSuccessful() {
        return successMessage.isVisible();
    }
    
    /**
     * Get transaction ID
     * @return Transaction ID
     */
    public String getTransactionId() {
        return transactionId.textContent();
    }
    
    /**
     * Check if error message is displayed
     * @return true if error
     */
    public boolean hasError() {
        return errorMessage.isVisible();
    }
    
    /**
     * Get error message
     * @return Error message text
     */
    public String getErrorMessage() {
        return errorMessage.textContent();
    }
    
    /**
     * Verify transfer page is displayed
     * @return this for method chaining
     */
    public TransferPage verifyTransferPageDisplayed() {
        assertThat(heading).isVisible();
        assertThat(fromAccountDropdown).isVisible();
        assertThat(toAccountInput).isVisible();
        assertThat(amountInput).isVisible();
        assertThat(transferButton).isVisible();
        return this;
    }
    
    /**
     * Verify transfer success
     * @return this for method chaining
     */
    public TransferPage verifyTransferSuccessful() {
        assertThat(successMessage).isVisible();
        assertThat(transactionId).isVisible();
        return this;
    }
    
    /**
     * Verify error message
     * @param expectedError Expected error message
     * @return this for method chaining
     */
    public TransferPage verifyErrorMessage(String expectedError) {
        assertThat(errorMessage).isVisible();
        assertThat(errorMessage).containsText(expectedError);
        return this;
    }
    
    /**
     * Verify transfer button is enabled
     * @return this for method chaining
     */
    public TransferPage verifyTransferButtonEnabled() {
        assertThat(transferButton).isEnabled();
        return this;
    }
    
    /**
     * Go back to dashboard
     * @return DashboardPage
     */
    public DashboardPage backToDashboard() {
        page.getByRole(AriaRole.LINK, 
            new Page.GetByRoleOptions().setName("Back to Dashboard"))
            .click();
        return new DashboardPage(page);
    }
}

/*
 * TRANSFER PAGE PATTERN:
 * 
 * 1. ALL-IN-ONE METHOD:
 *    public TransferPage doTransfer(...) {
 *        // All steps combined
 *    }
 *    // Useful for common scenarios
 * 
 * 2. INDIVIDUAL METHODS:
 *    enterAmount()
 *    enterRemarks()
 *    // Flexible for different test scenarios
 * 
 * 3. VERIFICATION METHODS:
 *    verifyTransferSuccessful()
 *    verifyErrorMessage()
 *    // Keep assertions in page object
 * 
 * 4. USAGE:
 *    new TransferPage(page)
 *        .verifyTransferPageDisplayed()
 *        .doTransfer("savings", "9876543210", "5000", "Rent")
 *        .verifyTransferSuccessful();
 * 
 * 5. BENEFITS:
 *    ✅ Fluent API (method chaining)
 *    ✅ Readable tests
 *    ✅ All-in-one for common scenarios
 *    ✅ Individual methods for flexibility
 */
