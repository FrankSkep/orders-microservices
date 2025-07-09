package com.frank.paymentservice.exception;

public class OrderException extends RuntimeException {
    public OrderException(String message) {
        super(message);
    }
}
