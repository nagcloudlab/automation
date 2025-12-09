package com.upi.service;

import com.upi.dto.TransferRequest;
import com.upi.dto.TransferResponse;

/**
 * Service interface for fund transfer operations.
 */
public interface TransferService {

    /**
     * Transfer funds between two accounts.
     * 
     * @param request Transfer request containing source, destination, and amount
     * @return TransferResponse with transaction details
     */
    TransferResponse transferFunds(TransferRequest request);
}
