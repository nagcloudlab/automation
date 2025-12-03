package com.npci.level10.model;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

/**
 * Level 10: Streams API - Customer Model
 */
public class Customer {

    private String customerId;
    private String name;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private String panNumber;
    private LocalDate registrationDate;
    private String customerType;  // REGULAR, PREMIUM, VIP
    private String city;
    private String state;
    private boolean isActive;
    private double annualIncome;

    // ═══════════════════════════════════════════════════════════════
    // CONSTRUCTORS
    // ═══════════════════════════════════════════════════════════════

    public Customer(String customerId, String name, String email, String phone) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.registrationDate = LocalDate.now();
        this.customerType = "REGULAR";
        this.isActive = true;
    }

    public Customer(String customerId, String name, String email, String phone,
                    LocalDate dateOfBirth, String city, String state,
                    String customerType, double annualIncome) {
        this(customerId, name, email, phone);
        this.dateOfBirth = dateOfBirth;
        this.city = city;
        this.state = state;
        this.customerType = customerType;
        this.annualIncome = annualIncome;
    }

    // ═══════════════════════════════════════════════════════════════
    // GETTERS AND SETTERS
    // ═══════════════════════════════════════════════════════════════

    public String getCustomerId() { return customerId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dob) { this.dateOfBirth = dob; }
    public String getPanNumber() { return panNumber; }
    public void setPanNumber(String pan) { this.panNumber = pan; }
    public LocalDate getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDate date) { this.registrationDate = date; }
    public String getCustomerType() { return customerType; }
    public void setCustomerType(String type) { this.customerType = type; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { this.isActive = active; }
    public double getAnnualIncome() { return annualIncome; }
    public void setAnnualIncome(double income) { this.annualIncome = income; }

    // ═══════════════════════════════════════════════════════════════
    // DERIVED PROPERTIES
    // ═══════════════════════════════════════════════════════════════

    public int getAge() {
        if (dateOfBirth == null) return 0;
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    public int getYearsAsCustomer() {
        if (registrationDate == null) return 0;
        return Period.between(registrationDate, LocalDate.now()).getYears();
    }

    public boolean isVIP() {
        return "VIP".equals(customerType);
    }

    public boolean isPremium() {
        return "PREMIUM".equals(customerType);
    }

    // ═══════════════════════════════════════════════════════════════
    // equals, hashCode, toString
    // ═══════════════════════════════════════════════════════════════

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Customer customer = (Customer) obj;
        return Objects.equals(customerId, customer.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId);
    }

    @Override
    public String toString() {
        return String.format("Customer[%s, %s, %s, %s, %s]",
                customerId, name, city, customerType, isActive ? "Active" : "Inactive");
    }
}