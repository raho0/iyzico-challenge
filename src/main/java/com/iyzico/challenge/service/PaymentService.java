package com.iyzico.challenge.service;

import com.iyzico.challenge.dto.PaymentRequest;
import com.iyzico.challenge.dto.PaymentResponse;
import com.iyzico.challenge.entity.Flight;
import com.iyzico.challenge.entity.Passenger;
import com.iyzico.challenge.entity.Seat;
import com.iyzico.challenge.exception.SoldSeatException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

@Service
public class PaymentService {

    private final SeatService seatService;
    private final FlightService flightService;
    private final PaymentServiceClients paymentServiceClients;


    public PaymentService(SeatService seatService, FlightService flightService, PaymentServiceClients paymentServiceClients) {
        this.seatService = seatService;
        this.flightService = flightService;
        this.paymentServiceClients = paymentServiceClients;
    }


    @Transactional
    public PaymentResponse process(PaymentRequest paymentRequest) {

        Passenger passenger = new Passenger(paymentRequest.getPassengerName());

        Flight flight = flightService.getById(paymentRequest.getFlightId());

        Seat selectedSeat = seatService.getByIdWithLock(paymentRequest.getSelectedSeatId());

        if (selectedSeat.isSold())
            throw new SoldSeatException(selectedSeat.getId());

        CompletableFuture<String> call = paymentServiceClients.call(selectedSeat.getPrice());

        if (!call.isDone())
            throw new RuntimeException("Payment failed");

        selectedSeat.setSold(true);
        selectedSeat.setPassenger(passenger);
        seatService.save(selectedSeat);

        return new PaymentResponse(passenger.getName(), flight.getId(), flight.getName(), selectedSeat.getId(), selectedSeat.getName());
    }
}
