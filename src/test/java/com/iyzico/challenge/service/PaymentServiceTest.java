package com.iyzico.challenge.service;

import com.iyzico.challenge.dto.PaymentRequest;
import com.iyzico.challenge.dto.PaymentResponse;
import com.iyzico.challenge.entity.Flight;
import com.iyzico.challenge.entity.Seat;
import com.iyzico.challenge.exception.SoldSeatException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {
    @Mock
    private FlightService flightService;

    @Mock
    private SeatService seatService;

    @Mock
    private PaymentServiceClients paymentServiceClients;

    @InjectMocks
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        // Reset the mocks before each test
        Mockito.reset(flightService, seatService, paymentServiceClients);
    }

    @Test
    void testProcess_withValidRequest_shouldReturnPaymentResponse() throws Exception {
        // Setup mock data
        PaymentRequest request = new PaymentRequest();
        request.setPassengerName("John Doe");
        request.setFlightId(1L);
        request.setSelectedSeatId(2L);

        Flight flight = new Flight();
        flight.setId(1L);
        flight.setName("Flight 1");

        Seat seat = new Seat(2L, "Seat 2", BigDecimal.valueOf(100.0));
        CompletableFuture<String> future = CompletableFuture.completedFuture("success");

        // Configure mock behavior
        Mockito.when(flightService.getById(1L)).thenReturn(flight);
        Mockito.when(seatService.getByIdWithLock(2L)).thenReturn(seat);
        Mockito.when(paymentServiceClients.call(seat.getPrice())).thenReturn(future);

        // Call the method being tested
        PaymentResponse response = paymentService.process(request);

        // Verify the results
        Assertions.assertNotNull(response);
        Assertions.assertEquals("John Doe", response.getPassengerName());
        Assertions.assertEquals(flight.getId(), response.getFlightId());
        Assertions.assertEquals(flight.getName(), response.getFlightName());
        Assertions.assertEquals(seat.getId(), response.getSelectedSeatId());
        Assertions.assertEquals(seat.getName(), response.getSelectedSeatName());

        Assertions.assertTrue(seat.isSold());
        Assertions.assertEquals("John Doe", seat.getPassenger().getName());

        Mockito.verify(flightService).getById(1L);
        Mockito.verify(seatService).getByIdWithLock(2L);
        Mockito.verify(paymentServiceClients).call(seat.getPrice());
        Mockito.verify(seatService).save(seat);
    }

    @Test
    void testProcess_withSoldSeat_shouldThrowSoldSeatException() throws Exception {
        // Setup mock data
        PaymentRequest request = new PaymentRequest();
        request.setFlightId(1L);
        request.setSelectedSeatId(2L);
        request.setPassengerName("test name");

        Flight flight = new Flight();
        flight.setId(1L);
        flight.setName("test");

        Seat seat = new Seat();
        seat.setId(2L);
        seat.setFlight(flight);
        seat.setName("test seat");
        seat.setSold(true);

        // Configure mock behavior
        Mockito.when(flightService.getById(1L)).thenReturn(flight);
        Mockito.when(seatService.getByIdWithLock(2L)).thenReturn(seat);

        // Call the method being tested and verify the exception thrown
        Assertions.assertThrows(SoldSeatException.class, () -> paymentService.process(request));

        Mockito.verify(flightService).getById(1L);
        Mockito.verify(seatService).getByIdWithLock(2L);
        Mockito.verifyNoMoreInteractions(paymentServiceClients, seatService);
    }

    @Test
    void testProcess_withPaymentFailure_shouldThrowRuntimeException() throws Exception {

        PaymentRequest request = new PaymentRequest();
        request.setPassengerName("John Doe");
        request.setFlightId(1L);
        request.setSelectedSeatId(2L);

        Flight flight = new Flight();
        flight.setId(1L);
        flight.setName("Flight 1");

        Seat seat = new Seat(2L, "Seat 2", BigDecimal.valueOf(100.0));
        CompletableFuture<String> future = new CompletableFuture<>();

        // Configure mock behavior
        Mockito.when(flightService.getById(1L)).thenReturn(flight);
        Mockito.when(seatService.getByIdWithLock(2L)).thenReturn(seat);
        Mockito.when(paymentServiceClients.call(seat.getPrice())).thenReturn(future);

        // Call the method being tested and verify the exception thrown
        Assertions.assertThrows(RuntimeException.class, () -> paymentService.process(request));

        Assertions.assertFalse(seat.isSold());

        Mockito.verify(flightService).getById(1L);
        Mockito.verify(seatService).getByIdWithLock(2L);
        Mockito.verify(paymentServiceClients).call(seat.getPrice());
        Mockito.verifyNoMoreInteractions(seatService);
    }
}