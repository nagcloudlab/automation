package org.example;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@XmlAccessorType(value = XmlAccessType.FIELD)
@Setter
@Getter
public class TransferCustomer {

    @XmlElement
    private String customerId;

    @XmlElement
    private String name;

    public TransferCustomer() {}
}