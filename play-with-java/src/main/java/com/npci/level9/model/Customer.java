package com.npci.level9.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Level 9: Collections Framework - Customer Model
 *
 * Key Concepts for Collections:
 * - equals() and hashCode() - Required for Set and Map operations
 * - Comparable interface - Natural ordering for sorting
 * - Immutable fields for hash-based collections
 */
public class Customer implements Comparable<Customer> {

    private final String customerId;  // Immutable - used in hashCode
    private String name;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private String panNumber;
    private LocalDate registrationDate;
    private String customerType;  // REGULAR, PREMIUM, VIP

    // ═══════════════════════════════════════════════════════════════
    // CONSTRUCTOR
    // ═══════════════════════════════════════════════════════════════

    public Customer(String customerId, String name, String email, String phone) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.registrationDate = LocalDate.now();
        this.customerType = "REGULAR";
    }

    public Customer(String customerId, String name, String email, String phone,
                    LocalDate dateOfBirth, String panNumber) {
        this(customerId, name, email, phone);
        this.dateOfBirth = dateOfBirth;
        this.panNumber = panNumber;
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
    public String getCustomerType() { return customerType; }
    public void setCustomerType(String type) { this.customerType = type; }

    // ═══════════════════════════════════════════════════════════════
    // equals() and hashCode() - CRITICAL for HashSet, HashMap
    // Two customers are equal if they have the same customerId
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

    // ═══════════════════════════════════════════════════════════════
    // Comparable - Natural ordering by customerId
    // Used by TreeSet, TreeMap, Collections.sort()
    // ═══════════════════════════════════════════════════════════════

    @Override
    public int compareTo(Customer other) {
        return this.customerId.compareTo(other.customerId);
    }

    // ═══════════════════════════════════════════════════════════════
    // toString
    // ═══════════════════════════════════════════════════════════════

    @Override
    public String toString() {
        return String.format("Customer[%s, %s, %s, %s]",
                customerId, name, email, customerType);
    }

    public String toDetailedString() {
        return String.format(
                "Customer ID: %s\n  Name: %s\n  Email: %s\n  Phone: %s\n  Type: %s\n  Since: %s",
                customerId, name, email, phone, customerType, registrationDate);
    }
}