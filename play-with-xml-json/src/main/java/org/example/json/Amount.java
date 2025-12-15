package org.example.json;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Amount {

    private String currency;
    private double value;

    public Amount() {}
}
