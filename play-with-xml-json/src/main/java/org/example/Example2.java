package org.example;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;

public class XMLToJavaObject {
    public static void main(String[] args) throws JAXBException {

        JAXBContext context = JAXBContext.newInstance(Customer.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();

        Customer customer =
                (Customer) unmarshaller.unmarshal(new File("/Users/nag/automation/play-with-xml-json/src/main/resources/customer.xml"));

        System.out.println(customer);
    }
}
