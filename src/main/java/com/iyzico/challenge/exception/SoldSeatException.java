package com.iyzico.challenge.exception;

public class SoldSeatException extends RuntimeException {

    public SoldSeatException(Long id) {
        super(String.format("The seat with id: %d has already been sold", id));
    }
}
