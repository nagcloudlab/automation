package org.example.json;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Transfer {

    private String fromAccount;
    private String toAccount;
    private Amount amount;
    private TransferStatus status;

    public Transfer() {}
}
