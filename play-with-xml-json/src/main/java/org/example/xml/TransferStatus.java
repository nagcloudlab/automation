package org.example;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

@XmlType
@XmlEnum
public enum TransferStatus {

    @XmlEnumValue("SUCCESS")
    SUCCESS,

    @XmlEnumValue("PENDING")
    PENDING,

    @XmlEnumValue("FAILED")
    FAILED
}
