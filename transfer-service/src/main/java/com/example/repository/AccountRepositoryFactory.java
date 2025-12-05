package com.example.repository;

// Factory class to provide AccountRepository instances
public class AccountRepositoryFactory {

    // Factory method to get AccountRepository based on type
    public static AccountRepository getAccountRepository(String type) {
        if (type.equalsIgnoreCase("sql")) {
            return new SqlAccountRepository();
        } else {
            throw new IllegalArgumentException("Unknown repository type: " + type);
        }
    }

}
