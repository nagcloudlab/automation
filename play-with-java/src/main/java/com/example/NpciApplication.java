package com.example;

public class NpciApplication {

    public static void main(String[] args) {

        // ---- Account(s) ----

        Account savingsAccount = new SavingsAccount("SAV123456", "John Doe", 5000.00, 5);
        Account currentAccount = new CurrentAccount("CUR123456", "Jane Smith", 10000.00, 2000.00);
        Account fdAccount = new FixedDepositAccount("FD123456", "Alice Johnson", 20000.00, 12, 6);

        // ---- Payment Services ----
        PaymentService upiPaymentService = new UPIPayment("s1@oksbi", "r1@okicic", 100.00, "1234");
        PaymentService impsPaymentService = new IMPSPayment("9876543210", "0123456789", "CAN00000163", 100.00);

    }

}
