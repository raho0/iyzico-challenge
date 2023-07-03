package com.iyzico.challenge.exception;

public class RecordNotFoundException extends RuntimeException {

    public RecordNotFoundException(Class<?> clazz, Long id) {
        super(String.format("%s not found with id: %d ", clazz.getSimpleName(), id));
    }
}
