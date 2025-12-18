package com.demo.transfer.controller;

import com.demo.transfer.model.TransferRequest;
import com.demo.transfer.model.TransferResponse;
import com.demo.transfer.service.TransferService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transfer")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    public ResponseEntity<TransferResponse> transfer(
            @Valid @RequestBody TransferRequest request) {

        TransferResponse response = transferService.processTransfer(request);
        return ResponseEntity.ok(response);
    }
}
