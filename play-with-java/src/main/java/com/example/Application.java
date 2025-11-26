package com.example;

public class Application {

    public static void main(String[] args) {

        System.out.println(Thread.currentThread().getName() + " : Application started");

        Account a1 = new Account();
        // display account details
        System.out.println("Account Number: " + a1.accountNumber);
        System.out.println("Holder Name: " + a1.holderName);
        System.out.println("Balance: " + a1.balance);

        // set account details
        a1.accountNumber = "ACC12345";
        a1.holderName = "John Doe";
        a1.balance = 1000.50;

        // display updated account details
        System.out.println("Updated Account Number: " + a1.accountNumber);
        System.out.println("Updated Holder Name: " + a1.holderName);
        System.out.println("Updated Balance: " + a1.balance);

        // deposit amount
        double depositAmount = 500.00;
        a1.balance += depositAmount;
        System.out.println("Balance after depositing " + depositAmount + ": " + a1.balance);

    }

}
