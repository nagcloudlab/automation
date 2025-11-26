package com.example;

public class Application {

    public static void main(String[] args) {

        System.out.println(Thread.currentThread().getName() + " : Application started");

        Account a1 = new Account("A101");
        a1.setHolderName("Nagendra");

        Account a2 = new Account("A102", "Riya");

        Account a3 = new Account("A103", "dia", 9000);

        // -------------------------------
        // Junor Developer
        // -------------------------------

        a1.deposit(2000);
        a1.withdraw(1000);

        a2.deposit(3000);
        a2.withdraw(12000);

        a3.deposit(4000);
        a3.withdraw(5000);

        System.out.println("------------------");

        System.out.println("Account Number: " + a1.getAccountNumber());
        System.out.println("Holder Name: " + a1.getHolderName());
        System.out.println("Balance: Rs." + a1.getBalance());

        System.out.println("------------------");

    }

}
