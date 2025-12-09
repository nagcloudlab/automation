package com.upi.config;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import com.upi.model.Account;
import com.upi.model.AccountStatus;
import com.upi.model.AccountType;
import com.upi.model.Transaction;
import com.upi.model.TransactionStatus;
import com.upi.model.TransactionType;
import com.upi.repository.AccountRepository;
import com.upi.repository.TransactionRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * Configuration class to load sample data into the database.
 * This data is useful for REST Assured testing and demonstration purposes.
 */
@Configuration
@Slf4j
public class DataLoader {

    @Bean
    @Order(1)
    CommandLineRunner loadSampleData(AccountRepository accountRepository, 
                                      TransactionRepository transactionRepository) {
        return args -> {
            log.info("Loading sample data...");
            
            // Create sample accounts
            Account account1 = createAccount("A001", "John Doe", "john.doe@example.com", 
                    "9876543210", new BigDecimal("15000.00"), AccountType.SAVINGS);
            Account account2 = createAccount("A002", "Jane Smith", "jane.smith@example.com", 
                    "9876543211", new BigDecimal("25000.50"), AccountType.CURRENT);
            Account account3 = createAccount("A003", "Alice Johnson", "alice.j@example.com", 
                    "9876543212", new BigDecimal("30000.75"), AccountType.SAVINGS);
            Account account4 = createAccount("A004", "Bob Wilson", "bob.wilson@example.com", 
                    "9876543213", new BigDecimal("5000.00"), AccountType.SALARY);
            Account account5 = createAccount("A005", "Carol Davis", "carol.davis@example.com", 
                    "9876543214", new BigDecimal("75000.00"), AccountType.CURRENT);
            
            // Create an inactive account for testing
            Account account6 = createAccount("A006", "Inactive User", "inactive@example.com", 
                    "9876543215", new BigDecimal("1000.00"), AccountType.SAVINGS);
            account6.setStatus(AccountStatus.INACTIVE);
            
            // Create a suspended account for testing
            Account account7 = createAccount("A007", "Suspended User", "suspended@example.com", 
                    "9876543216", new BigDecimal("500.00"), AccountType.SAVINGS);
            account7.setStatus(AccountStatus.SUSPENDED);

            // Save all accounts
            account1 = accountRepository.save(account1);
            account2 = accountRepository.save(account2);
            account3 = accountRepository.save(account3);
            account4 = accountRepository.save(account4);
            account5 = accountRepository.save(account5);
            account6 = accountRepository.save(account6);
            account7 = accountRepository.save(account7);

            log.info("Created {} sample accounts", 7);

            // Create sample transactions
            Transaction txn1 = createTransaction("TXN1001", account1, account2, 
                    new BigDecimal("500.00"), "Monthly rent payment", TransactionStatus.COMPLETED);
            Transaction txn2 = createTransaction("TXN1002", account2, account3, 
                    new BigDecimal("1000.00"), "Invoice payment", TransactionStatus.COMPLETED);
            Transaction txn3 = createTransaction("TXN1003", account3, account1, 
                    new BigDecimal("250.50"), "Reimbursement", TransactionStatus.COMPLETED);
            Transaction txn4 = createTransaction("TXN1004", account4, account5, 
                    new BigDecimal("100.00"), "Subscription fee", TransactionStatus.COMPLETED);
            Transaction txn5 = createTransaction("TXN1005", account5, account4, 
                    new BigDecimal("2000.00"), "Salary advance", TransactionStatus.COMPLETED);
            Transaction txn6 = createTransaction("TXN1006", account1, account3, 
                    new BigDecimal("750.00"), "Gift", TransactionStatus.COMPLETED);
            
            // Create a failed transaction for testing
            Transaction txn7 = createTransaction("TXN1007", account4, account2, 
                    new BigDecimal("10000.00"), "Large transfer attempt", TransactionStatus.FAILED);
            txn7.setFailureReason("Insufficient funds");

            // Save all transactions
            transactionRepository.save(txn1);
            transactionRepository.save(txn2);
            transactionRepository.save(txn3);
            transactionRepository.save(txn4);
            transactionRepository.save(txn5);
            transactionRepository.save(txn6);
            transactionRepository.save(txn7);

            log.info("Created {} sample transactions", 7);
            log.info("Sample data loaded successfully!");
            
            // Print account summary
            printAccountSummary(accountRepository);
        };
    }

    private Account createAccount(String accountId, String holderName, String email, 
                                   String phone, BigDecimal balance, AccountType type) {
        return Account.builder()
                .accountId(accountId)
                .accountHolderName(holderName)
                .email(email)
                .phone(phone)
                .balance(balance)
                .accountType(type)
                .status(AccountStatus.ACTIVE)
                .build();
    }

    private Transaction createTransaction(String refNumber, Account from, Account to,
                                          BigDecimal amount, String description, 
                                          TransactionStatus status) {
        return Transaction.builder()
                .referenceNumber(refNumber)
                .fromAccount(from)
                .toAccount(to)
                .amount(amount)
                .description(description)
                .transactionType(TransactionType.FUND_TRANSFER)
                .status(status)
                .fromBalanceBefore(from.getBalance())
                .fromBalanceAfter(from.getBalance().subtract(amount))
                .toBalanceBefore(to.getBalance())
                .toBalanceAfter(to.getBalance().add(amount))
                .build();
    }

    private void printAccountSummary(AccountRepository accountRepository) {
        log.info("========================================");
        log.info("SAMPLE ACCOUNTS SUMMARY");
        log.info("========================================");
        accountRepository.findAll().forEach(account -> {
            log.info("Account: {} | Holder: {} | Balance: {} | Status: {}",
                    account.getAccountId(),
                    account.getAccountHolderName(),
                    account.getBalance(),
                    account.getStatus());
        });
        log.info("========================================");
    }
}
