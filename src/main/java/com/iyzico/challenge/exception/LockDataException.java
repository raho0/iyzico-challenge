package com.iyzico.challenge.exception;

public class LockDataException extends RuntimeException {

    public LockDataException(Class<?> clazz, Long id) {
        super(String.format("Entity %s with id: %d is being used by another transaction.", clazz.getSimpleName(), id));
    }
}
