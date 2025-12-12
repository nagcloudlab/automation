package org.example;


import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

public class Example1 {
    public static void main(String[] args) throws JAXBException {

        Customer customer = new Customer(101, "Nag", "nag@npci.org");

        JAXBContext context = JAXBContext.newInstance(Customer.class);

        Marshaller marshaller = context.createMarshaller();

        // Pretty print
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        // Output to console
//        marshaller.marshal(customer, System.out);

        String xmlString;
        try (java.io.StringWriter sw = new java.io.StringWriter()) {
            marshaller.marshal(customer, sw);
            xmlString = sw.toString();
        } catch (java.io.IOException e) {
            throw new RuntimeException("Error while converting to XML", e);
        }

        System.out.println(xmlString);


    }
}
