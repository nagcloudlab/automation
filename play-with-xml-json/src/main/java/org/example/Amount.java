package org.example;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlValue;
import lombok.Getter;
import lombok.Setter;

@XmlAccessorType(XmlAccessType.FIELD)
@Setter
@Getter
public class Amount {

    @XmlAttribute
    private String currency;

    @XmlValue
    private double value;

    public Amount() {}

    public Amount(String currency, double value) {
        this.currency = currency;
        this.value = value;
    }
}
