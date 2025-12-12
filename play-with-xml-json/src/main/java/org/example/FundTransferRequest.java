package org.example;

import jakarta.xml.bind.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@XmlRootElement(
        name = "FundTransferRequest",
        namespace = "http://npci.org/fundtransfer"
)
@XmlAccessorType(XmlAccessType.FIELD)
@Setter
@Getter
public class FundTransferRequest {

    @XmlElement
    private Header header;

    @XmlElement
    private TransferCustomer customer;

    @XmlElementWrapper(name = "transfers")
    @XmlElement(name = "transfer")
    private List<Transfer> transfers;

    public FundTransferRequest() {}
}
