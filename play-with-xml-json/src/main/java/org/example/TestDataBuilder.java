package org.example;


import java.time.LocalDateTime;
import java.util.List;

public class TestDataBuilder {

    public static FundTransferRequest build() {

        // Header
//        Header header = new Header();
//        header.setRequestId("REQ-" + System.currentTimeMillis());
//        header.setChannel("UPI");
//        header.setTimestamp(LocalDateTime.now());
//
//        // Customer
//        Customer customer = new Customer();
//        customer.setCustomerId("CUST1001");
//        customer.setName("Nagabhushanam");

        // Transfers
        Transfer t1 = createTransfer(
                "123456",
                "654321",
                5000,
                "INR",
                TransferStatus.SUCCESS
        );

        Transfer t2 = createTransfer(
                "123456",
                "777777",
                2500,
                "INR",
                TransferStatus.PENDING
        );

        // Request
//        FundTransferRequest request = new FundTransferRequest();
//        request.setHeader(header);
//        request.setCustomer(customer);
//        request.setTransfers(List.of(t1, t2));

//        return request;
        return null;
    }

    private static Transfer createTransfer(
            String from,
            String to,
            double amountValue,
            String currency,
            TransferStatus status) {

//        Amount amount = new Amount();
//        amount.setCurrency(currency);
//        amount.setValue(amountValue);
//
//        Transfer transfer = new Transfer();
//        transfer.setFromAccount(from);
//        transfer.setToAccount(to);
//        transfer.setAmount(amount);
//        transfer.setStatus(status);

//        return transfer;
        return  null;
    }

}
