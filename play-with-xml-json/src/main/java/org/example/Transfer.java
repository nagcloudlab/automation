package org.example;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Getter;
import lombok.Setter;

@XmlAccessorType(XmlAccessType.FIELD)
@Setter
@Getter
public class Transfer {

    @XmlElement
    private String fromAccount;

    @XmlElement
    private String toAccount;

    @XmlElement
    private Amount amount;

    @XmlElement
    private TransferStatus status;

    public Transfer() {}
}
