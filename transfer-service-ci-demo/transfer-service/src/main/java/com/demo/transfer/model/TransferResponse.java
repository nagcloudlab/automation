package com.demo.transfer.model;

import java.time.Instant;
import java.util.UUID;

public class TransferResponse {

    private String transactionId;
    private String status;
    private Instant timestamp;

    public TransferResponse(String status) {
        this.transactionId = UUID.randomUUID().toString();
        this.status = status;
        this.timestamp = Instant.now();
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getStatus() {
        return status;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
