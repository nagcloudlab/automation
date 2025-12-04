package com.example.service;

import com.example.model.Account;
import com.example.repository.SqlAccountRepository;

public class UPITransferService {

    public UPITransferService() {
        System.out.println("UPITransferService instance created.");
    }

    public void transfer(double amount, String fromAccountId, String toAccountId) {

        System.out.println("Initiating UPI transfer of amount: " + amount + " from account: " + fromAccountId + " to account: " + toAccountId);

        SqlAccountRepository accountRepository = new SqlAccountRepository();

        // step-1: Load 'from' account details from DB
        Account fromAccount = accountRepository.loadAccountById(fromAccountId);
        // step-2: Load 'to' account details from DB
        Account toAccount = accountRepository.loadAccountById(toAccountId);

        // step-3: Debit 'from' account
        fromAccount.debit(amount);
        // step-4: Credit 'to' account
        toAccount.credit(amount);

        // step-5: Update 'from' account details in DB
        accountRepository.saveAccount(fromAccount);

        // step-6: Update 'to' account details in DB
        accountRepository.saveAccount(toAccount);

        System.out.println("UPI transfer completed successfully.");


    }
}
