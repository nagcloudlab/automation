package com.example;

import com.example.repository.AccountRepository;
import com.example.repository.AccountRepositoryFactory;
import com.example.service.UPITransferService;

public class Application {
    public static void main(String[] args) {


        //---------------------------------
        // Initialize Phase
        //---------------------------------
        System.out.println("-".repeat(50));
        AccountRepository sqlAccountRepository = AccountRepositoryFactory.getAccountRepository("sql");
        UPITransferService upiTransferService = new UPITransferService(sqlAccountRepository);

        System.out.println("-".repeat(50));
        //---------------------------------
        // Use Phase
        //---------------------------------

        upiTransferService.transfer(300.00, "123", "456");
        System.out.println("--");
        upiTransferService.transfer(100.00, "789", "012");

        System.out.println("-".repeat(50));
        //---------------------------------
        // Cleanup Phase
        //---------------------------------

        //....


    }
}
