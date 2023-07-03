package com.iyzico.challenge.service;

import com.iyzico.challenge.dto.FlightCreateRequest;
import com.iyzico.challenge.dto.FlightUpdateRequest;
import com.iyzico.challenge.dto.SeatCreateRequest;
import com.iyzico.challenge.entity.Flight;
import com.iyzico.challenge.entity.Seat;
import com.iyzico.challenge.exception.RecordNotFoundException;
import com.iyzico.challenge.repository.FlightRepository;
import com.iyzico.challenge.util.ConverterUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private FlightService flightService;

    @Test
    void testAdd() {

        FlightCreateRequest createRequest = new FlightCreateRequest();
        createRequest.setName("test");
        createRequest.setDescription("test description");

        SeatCreateRequest seat1 = new SeatCreateRequest();
        seat1.setName("seat1");
        seat1.setPrice(new BigDecimal(100));
        createRequest.setSeats(Collections.singletonList(seat1));

        Flight flight = new Flight();
        flight.setId(1L);
        flight.setName("test");
        flight.setDescription("test description");

        Seat seat = new Seat();
        seat.setId(1L);
        seat.setName("seat1");
        seat.setPrice(new BigDecimal(100));
        seat.setFlight(flight);
        flight.setSeats(Collections.singletonList(seat));

        Mockito.when(flightRepository.save(Mockito.any(Flight.class))).thenReturn(flight);

        Flight result = flightService.add(createRequest);

        Assertions.assertEquals(flight, result);
    }

    @Test
    void testSave() {
        Flight flight = new Flight();
        flight.setId(1L);
        flight.setName("test");
        flight.setDescription("test description");

        Seat seat = new Seat();
        seat.setId(1L);
        seat.setName("seat1");
        seat.setPrice(new BigDecimal(100));
        seat.setFlight(flight);
        flight.setSeats(Collections.singletonList(seat));

        Mockito.when(flightRepository.save(ArgumentMatchers.any(Flight.class))).thenReturn(flight);

        Flight result = flightService.save(flight);

        Assertions.assertNotNull(result);
    }

    @Test
    void testUpdate_existingFlight_shouldUpdateAndReturnUpdatedFlight() {

        Flight existingFlight = new Flight();
        existingFlight.setId(1L);
        existingFlight.setName("test");
        existingFlight.setDescription("test description");

        Mockito.when(flightRepository.findById(1L)).thenReturn(Optional.of(existingFlight));

        FlightUpdateRequest updateRequest = new FlightUpdateRequest();
        updateRequest.setName("updated name");
        updateRequest.setDescription("updated description");

        Flight updatedFlight = ConverterUtil.toFlightEntity(existingFlight, updateRequest);
        Mockito.when(flightRepository.save(Mockito.any(Flight.class))).thenReturn(updatedFlight);

        Flight result = flightService.update(1L, updateRequest);

        Assertions.assertEquals(updatedFlight, result);
    }

    @Test
    void testUpdate_nonExistingFlight_shouldThrowRecordNotFoundException() {

        Mockito.when(flightRepository.findById(1L)).thenReturn(Optional.empty());

        FlightUpdateRequest updateRequest = new FlightUpdateRequest();
        updateRequest.setName("updated name");
        updateRequest.setDescription("updated description");

        Assertions.assertThrows(RecordNotFoundException.class, () -> {
            flightService.update(1L, updateRequest);
        });
    }

    @Test
    void testRemove_existingFlight_shouldDeleteFlight() {

        Flight existingFlight = new Flight();
        existingFlight.setId(1L);
        existingFlight.setName("test");
        existingFlight.setDescription("test description");

        Mockito.when(flightRepository.findById(1L)).thenReturn(Optional.of(existingFlight));

        flightService.remove(1L);

        Mockito.verify(flightRepository, Mockito.times(1)).delete(existingFlight);
    }

    @Test
    void testRemove_nonExistingFlight_shouldThrowRecordNotFoundException() {

        Mockito.when(flightRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(RecordNotFoundException.class, () -> {
            flightService.remove(1L);
        });

        Mockito.verify(flightRepository, Mockito.never()).delete(Mockito.any(Flight.class));
    }

    @Test
    void testGetById_existingFlight_shouldReturnFlight() {

        Flight existingFlight = new Flight();
        existingFlight.setId(1L);
        existingFlight.setName("test");
        existingFlight.setDescription("test description");

        Mockito.when(flightRepository.findById(1L)).thenReturn(Optional.of(existingFlight));

        Flight result = flightService.getById(1L);

        Assertions.assertEquals(existingFlight, result);
    }

    @Test
    void testGetById_nonExistingFlight_shouldThrowRecordNotFoundException() {

        Mockito.when(flightRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(RecordNotFoundException.class, () -> {
            flightService.getById(1L);
        });
    }

    @Test
    void testListById_existingFlight_shouldReturnAvailableSeats() {

        Seat seat1 = new Seat();
        seat1.setId(1L);
        seat1.setName("seat1");
        seat1.setPrice(new BigDecimal(100));
        seat1.setSold(false);

        Seat seat2 = new Seat();
        seat2.setId(2L);
        seat2.setName("seat2");
        seat2.setPrice(new BigDecimal(200));
        seat2.setSold(true);

        List<Seat> seats = Arrays.asList(seat1, seat2);

        Flight existingFlight = new Flight();
        existingFlight.setId(1L);
        existingFlight.setName("test");
        existingFlight.setDescription("test description");
        existingFlight.setSeats(seats);

        Mockito.when(flightRepository.findById(1L)).thenReturn(Optional.of(existingFlight));

        // Call the listById method on the FlightService and assert that it returns the correct Flight object with only 1 available seat
        Flight result = flightService.listById(1L);

        Assertions.assertEquals(1, result.getSeats().size());
        Assertions.assertEquals(new BigDecimal(100), result.getSeats().get(0).getPrice());
    }

    @Test
    void testListById_nonExistingFlight_shouldThrowRecordNotFoundException() {

        Mockito.when(flightRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(RecordNotFoundException.class, () -> {
            flightService.listById(1L);
        });
    }
}