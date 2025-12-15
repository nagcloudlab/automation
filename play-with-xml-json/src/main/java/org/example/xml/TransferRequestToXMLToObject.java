package org.example;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;

public class TransferRequestToXMLToObject {
//
//    public static void main(String[] args) throws JAXBException {
//
//        JAXBContext context =
//                JAXBContext.newInstance(FundTransferRequest.class);
//
//        Marshaller marshaller = context.createMarshaller();
//        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//
//        FundTransferRequest request = new FundTransferRequest();
//
//        Header header = new Header();
////        header.setRequestId("REQ-123");
////        header.setChannel("UPI");
////        header.setTimestamp(LocalDateTime.now());
//
////        Customer customer = new Customer();
////        customer.setCustomerId("CUST1001");
////        customer.setName("Nag");
////
////        Amount amount1 = new Amount();
////        amount1.setCurrency("INR");
////        amount1.setValue(5000);
////
////        Transfer t1 = new Transfer();
////        t1.setFromAccount("123456");
////        t1.setToAccount("654321");
////        t1.setAmount(amount1);
////        t1.setStatus(TransferStatus.SUCCESS);
//
//
//        marshaller.marshal(request, System.out);
//
//
//        Unmarshaller unmarshaller = context.createUnmarshaller();
//
//        FundTransferRequest request =
//                (FundTransferRequest) unmarshaller.unmarshal(
//                        new File("fund-transfer.xml")
//                );
//
//        System.out.println(request.getCustomer().getName());
//        System.out.println(request.getTransfers().size());



//    }
}
