package org.example.json;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FundTransferRequest {

    private Header header;
    private Customer customer;
    private List<Transfer> transfers;

    public FundTransferRequest() {}
}
