package com.upi.exception;

/**
 * Exception thrown when a transfer request is invalid.
 */
public class InvalidTransferException extends RuntimeException {

    public InvalidTransferException(String message) {
        super(message);
    }
}
