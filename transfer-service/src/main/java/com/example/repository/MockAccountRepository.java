package com.example.repository;

import com.example.model.Account;

public class MockAccountRepository implements AccountRepository{
    @Override
    public Account loadAccountById(String accountId) {
        Account account = new Account(accountId, "Mock User", 1000.0);
        return account;
    }

    @Override
    public void saveAccount(Account account) {
        // Mock implementation: do nothing
    }
}
