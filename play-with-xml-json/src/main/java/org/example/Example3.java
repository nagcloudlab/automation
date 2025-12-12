package org.example;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

import java.util.ArrayList;
import java.util.List;

public class Example3 {
    public static void main(String[] args) throws JAXBException {

        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer(1, "John Doe", "m@.com"));
        customers.add(new Customer(2, "Jane Smith", "j@.com"));

        CustomerList customerList = new CustomerList();
        customerList.setCustomers(customers);


        JAXBContext context = JAXBContext.newInstance(CustomerList.class);
        Marshaller marshaller = context.createMarshaller();
        // Pretty print
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        // Output to console
        marshaller.marshal(customerList, System.out);

    }
}
