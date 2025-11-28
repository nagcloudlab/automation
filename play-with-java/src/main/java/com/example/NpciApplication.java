package com.example;

public class NpciApplication {

    public static void main(String[] args) {

        PaymentService upiPaymentService = new UPIPayment("s1", "r1", 100.00, "1234");
        upiPaymentService.executePayment();
        PaymentService impsPaymentService = new IMPSPayment("9876543210", "0123456789", "CAN00163", 100.00);
        impsPaymentService.executePayment();

    }

}
