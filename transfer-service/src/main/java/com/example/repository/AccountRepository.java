package com.example.repository;

import com.example.model.Account;

public interface AccountRepository {
    public Account loadAccountById(String accountId);
    public void saveAccount(Account account);
}
