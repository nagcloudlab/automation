package org.example.xml;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import org.example.TransferStatus;

import java.io.File;
import java.util.List;

public class Example4 {
    public static void main(String[] args) throws JAXBException {

        FundTransferRequest transferRequest = new FundTransferRequest();

        Header header = new Header();
        header.setRequestId("REQ123456");
        header.setChannel("MOBILE");
        header.setTimestamp(java.time.LocalDateTime.now());

        TransferCustomer customer = new TransferCustomer();
        customer.setCustomerId("CUST78910");
        customer.setName("John Doe");


        Transfer transfer1 = new Transfer();
        transfer1.setAmount(new Amount("USD", 1500.00));
        transfer1.setFromAccount("ACC12345");
        transfer1.setToAccount("ACC67890");
        transfer1.setStatus(TransferStatus.PENDING);

        Transfer transfer2 = new Transfer();
        transfer2.setAmount(new Amount("USD", 2500.00));
        transfer2.setFromAccount("ACC54321");
        transfer2.setToAccount("ACC09876");
        transfer2.setStatus(TransferStatus.SUCCESS);

        List<Transfer> transfers = List.of(transfer1, transfer2);

        transferRequest.setHeader(header);
        transferRequest.setCustomer(customer);
        transferRequest.setTransfers(transfers);

        //-------------------------------------

//        JAXBContext context = JAXBContext.newInstance(FundTransferRequest.class);
//
//        Marshaller marshaller = context.createMarshaller();
//        // Pretty print
//        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//        // Output to console
//        marshaller.marshal(transferRequest, System.out);

        //-------------------------------------

        JAXBContext context = JAXBContext.newInstance(FundTransferRequest.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();

        FundTransferRequest transferRequest1 =
                (FundTransferRequest) unmarshaller.unmarshal(new File("/Users/nag/automation/play-with-xml-json/src/main/resources/transfer-request.xml"));

        System.out.println(transferRequest1.getCustomer().getName());

        //-------------------------------------

    }
}
