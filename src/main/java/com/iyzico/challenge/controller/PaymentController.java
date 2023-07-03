package com.iyzico.challenge.controller;

import com.iyzico.challenge.dto.PaymentRequest;
import com.iyzico.challenge.dto.PaymentResponse;
import com.iyzico.challenge.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping()
    public ResponseEntity<PaymentResponse> payment(
            @RequestBody PaymentRequest paymentRequest) {

        return new ResponseEntity<>(paymentService.process(paymentRequest), HttpStatus.CREATED);
    }
}
