package com.demo.transfer.service;

import com.demo.transfer.model.TransferRequest;
import com.demo.transfer.model.TransferResponse;
import org.springframework.stereotype.Service;

@Service
public class TransferService {

    public TransferResponse processTransfer(TransferRequest request) {

        // Simulate processing latency (useful for JMeter demo)
        try {
            Thread.sleep(50);
        } catch (InterruptedException ignored) {}

        return new TransferResponse("SUCCESS");
    }
}
