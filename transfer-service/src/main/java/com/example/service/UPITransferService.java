package com.example.service;

import com.example.model.Account;
import com.example.repository.AccountRepository;
import com.example.repository.AccountRepositoryFactory;
import com.example.repository.SqlAccountRepository;

/*

    design issues
    ---------------
    -> tight coupling b/w dependent class and dependency class
        => can't extend with new features easily
        => cant' test in isolation (unit testing difficult) -> dev or bug fix slow

    performance issues
    ------------------
    -> too many dependency instances created & left as garbage
        => memory consumption high
        => GC overhead high
        => overall application performance degrades


    why these issues?

    => dependent manages the lifecycle of dependency

    how to solve these design issues?

    => Don't create , get it from factory => Factory Pattern

    how to solve performance issues?

    => Don't create & Don't lookup , inject by some external entity ( Dependency Inversion Pattern )



 */

public class UPITransferService {

    private AccountRepository accountRepository; // HAS-A relationship

    // Dependency Injection via Constructor Injection
    public UPITransferService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
        System.out.println("UPITransferService instance created.");
    }

    public void transfer(double amount, String fromAccountId, String toAccountId) {

        System.out.println("Initiating UPI transfer of amount: " + amount + " from account: " + fromAccountId + " to account: " + toAccountId);

        //SqlAccountRepository accountRepository = new SqlAccountRepository(); // Dont'create
        // AccountRepository accountRepository = AccountRepositoryFactory.getAccountRepository("sql"); // Don't lookup

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
