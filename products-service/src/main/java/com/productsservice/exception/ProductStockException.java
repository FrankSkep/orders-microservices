package com.productsservice.exception;

public class ProductStockException extends RuntimeException {
    public ProductStockException(String message) {
        super(message);
    }
}
