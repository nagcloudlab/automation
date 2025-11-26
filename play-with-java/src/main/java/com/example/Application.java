package com.example;

public class Application {

    public static void main(String[] args) {

        SavingsAccount sa = new SavingsAccount("SA123", "Alice", 1000.0, 5.0);
        CurrentAccount ca = new CurrentAccount("CA123", "Bob", 2000.0, 500.0);

        PayentGateway pg = new PayentGateway();
        pg.processPayment(150.0, sa);
        pg.processPayment(300.0, ca);

    }

}
