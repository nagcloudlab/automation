package com.npci.training.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * UPIPaymentPage - Page Object for UPI Payment functionality
 * 
 * Represents UPI payment screen in Banking Portal
 * Handles all UPI payment operations
 */
public class UPIPaymentPage extends BasePage {
    
    // UPI Payment Form Locators
    private By vpaField = By.id("vpa");
    private By amountField = By.id("amount");
    private By remarksField = By.id("remarks");
    private By mobileField = By.id("mobile");
    private By upiPinField = By.id("upiPin");
    private By payNowButton = By.id("payNowBtn");
    private By verifyButton = By.id("verifyVpaBtn");
    private By cancelButton = By.cssSelector("button.cancel-btn");
    
    // Status and Message Locators
    private By statusMessage = By.id("paymentStatus");
    private By transactionId = By.id("transactionId");
    private By beneficiaryName = By.id("beneficiaryName");
    private By loadingSpinner = By.cssSelector(".loading-spinner");
    
    // Error Message Locators
    private By vpaError = By.id("vpaError");
    private By amountError = By.id("amountError");
    private By mobileError = By.id("mobileError");
    private By pinError = By.id("pinError");
    
    // Balance Display
    private By availableBalance = By.cssSelector(".available-balance");
    
    // Constructor
    public UPIPaymentPage(WebDriver driver) {
        super(driver);
    }
    
    // Navigation
    public UPIPaymentPage open() {
        driver.get("http://localhost:5500/upi-payment.html");
        waitForTitle("UPI Payment");
        return this;
    }
    
    // Actions - VPA (Virtual Payment Address)
    public UPIPaymentPage enterVPA(String vpa) {
        enterText(vpaField, vpa);
        return this;
    }
    
    public UPIPaymentPage clickVerifyVPA() {
        clickElement(verifyButton);
        // Wait for verification to complete
        waitForElementToDisappear(loadingSpinner);
        return this;
    }
    
    // Actions - Payment Details
    public UPIPaymentPage enterAmount(String amount) {
        enterText(amountField, amount);
        return this;
    }
    
    public UPIPaymentPage enterRemarks(String remarks) {
        enterText(remarksField, remarks);
        return this;
    }
    
    public UPIPaymentPage enterMobile(String mobile) {
        enterText(mobileField, mobile);
        return this;
    }
    
    public UPIPaymentPage enterUPIPin(String pin) {
        enterText(upiPinField, pin);
        return this;
    }
    
    // Actions - Payment Submission
    public UPIPaymentPage clickPayNow() {
        clickElement(payNowButton);
        // Wait for payment processing
        waitForElementToDisappear(loadingSpinner);
        return this;
    }
    
    public UPIPaymentPage clickCancel() {
        clickElement(cancelButton);
        return this;
    }
    
    // Complete Payment Flow
    public UPIPaymentPage makePayment(String vpa, String amount, String mobile, String pin) {
        return enterVPA(vpa)
               .clickVerifyVPA()
               .enterAmount(amount)
               .enterMobile(mobile)
               .enterUPIPin(pin)
               .clickPayNow();
    }
    
    public UPIPaymentPage makePaymentWithRemarks(String vpa, String amount, 
                                                  String mobile, String pin, String remarks) {
        return enterVPA(vpa)
               .clickVerifyVPA()
               .enterAmount(amount)
               .enterRemarks(remarks)
               .enterMobile(mobile)
               .enterUPIPin(pin)
               .clickPayNow();
    }
    
    // Information Retrieval
    public String getPaymentStatus() {
        return getElementText(statusMessage);
    }
    
    public String getTransactionId() {
        if (isElementPresent(transactionId)) {
            return getElementText(transactionId);
        }
        return null;
    }
    
    public String getBeneficiaryName() {
        if (isElementPresent(beneficiaryName)) {
            return getElementText(beneficiaryName);
        }
        return null;
    }
    
    public String getAvailableBalance() {
        return getElementText(availableBalance);
    }
    
    // Error Messages
    public String getVPAError() {
        if (isElementDisplayed(vpaError)) {
            return getElementText(vpaError);
        }
        return null;
    }
    
    public String getAmountError() {
        if (isElementDisplayed(amountError)) {
            return getElementText(amountError);
        }
        return null;
    }
    
    public String getMobileError() {
        if (isElementDisplayed(mobileError)) {
            return getElementText(mobileError);
        }
        return null;
    }
    
    public String getPinError() {
        if (isElementDisplayed(pinError)) {
            return getElementText(pinError);
        }
        return null;
    }
    
    // Verification Methods
    public boolean isPaymentSuccessful() {
        String status = getPaymentStatus();
        return status != null && status.contains("Success");
    }
    
    public boolean isPaymentFailed() {
        String status = getPaymentStatus();
        return status != null && status.contains("Failed");
    }
    
    public boolean isVPAValid() {
        String beneficiary = getBeneficiaryName();
        return beneficiary != null && !beneficiary.isEmpty();
    }
    
    public boolean isUPIPaymentPageDisplayed() {
        return isElementDisplayed(payNowButton) && 
               getCurrentUrl().contains("upi-payment.html");
    }
    
    public boolean hasValidationError() {
        return isElementDisplayed(vpaError) || 
               isElementDisplayed(amountError) ||
               isElementDisplayed(mobileError) ||
               isElementDisplayed(pinError);
    }
}
