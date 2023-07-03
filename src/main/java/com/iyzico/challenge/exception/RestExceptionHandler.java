package com.iyzico.challenge.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {RecordNotFoundException.class})
    protected ResponseEntity<Object> handleNotFoundException(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = ex.getMessage();

        return new ResponseEntity<>(bodyOfResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {SoldSeatException.class})
    protected ResponseEntity<Object> handleSoldedSeatException(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = ex.getMessage();

        return new ResponseEntity<>(bodyOfResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {LockDataException.class})
    protected ResponseEntity<Object> handleLockDataException(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = ex.getMessage();

        return new ResponseEntity<>(bodyOfResponse, HttpStatus.BAD_REQUEST);
    }

}