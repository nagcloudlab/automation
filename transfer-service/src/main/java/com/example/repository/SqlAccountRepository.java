package com.example.repository;

import com.example.model.Account;

public class SqlAccountRepository {

    public SqlAccountRepository(){
        System.out.println("SqlAccountRepository instance created.");
    }

    public Account loadAccountById(String accountId) {
        System.out.println("Loading account with ID: " + accountId + " from SQL database.");
        return new Account("ACC123", "John Doe", 1000.0);
    }

    public void saveAccount(Account account) {
        System.out.println("Saving account with ID: " + account.getAccountId() + " to SQL database.");
    }

}
