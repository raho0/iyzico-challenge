package com.iyzico.challenge.service;

import com.iyzico.challenge.dto.SeatCreateRequest;
import com.iyzico.challenge.dto.SeatUpdateRequest;
import com.iyzico.challenge.entity.Flight;
import com.iyzico.challenge.entity.Passenger;
import com.iyzico.challenge.entity.Seat;
import com.iyzico.challenge.exception.RecordNotFoundException;
import com.iyzico.challenge.repository.SeatRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class SeatServiceTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private FlightService flightService;

    @InjectMocks
    private SeatService seatService;

    @Test
    void testSave_validSeat_shouldSaveSeat() {

        Seat seat = new Seat();
        seat.setId(1L);
        seat.setName("test seat");
        seat.setPrice(new BigDecimal(100));
        seat.setSold(false);

        Mockito.when(seatRepository.save(seat)).thenReturn(seat);

        Seat result = seatService.save(seat);

        Assertions.assertEquals(seat, result);

        Mockito.verify(seatRepository, Mockito.times(1)).save(seat);
    }


    @Test
    void testAdd_validSeatCreateRequest_shouldReturnCreatedSeat() {

        Flight flight = new Flight();
        flight.setId(1L);
        flight.setName("test");
        flight.setDescription("test description");

        SeatCreateRequest seatCreateRequest = new SeatCreateRequest();
        seatCreateRequest.setName("test seat");
        seatCreateRequest.setFlightId(1L);
        seatCreateRequest.setPrice(new BigDecimal("120"));

        Seat expectedSeat = new Seat();
        expectedSeat.setId(1L);
        expectedSeat.setName("test seat");
        expectedSeat.setPrice(new BigDecimal("120"));
        expectedSeat.setFlight(flight);

        Mockito.when(flightService.getById(flight.getId())).thenReturn(flight);
        Mockito.when(seatRepository.save(Mockito.any(Seat.class))).thenReturn(expectedSeat);

        Seat actualSeat = seatService.add(seatCreateRequest);

        Mockito.verify(flightService, Mockito.times(1)).getById(flight.getId());
        Mockito.verify(flightService, Mockito.times(1)).save(flight);
        Mockito.verify(seatRepository, Mockito.times(1)).save(Mockito.any(Seat.class));
        Assertions.assertEquals(expectedSeat, actualSeat);
    }

    @Test
    void testAdd_nonExistingFlight_shouldThrowRecordNotFoundException() {

        SeatCreateRequest seatCreateRequest = new SeatCreateRequest();
        seatCreateRequest.setName("test seat");
        seatCreateRequest.setPrice(new BigDecimal(100));
        seatCreateRequest.setFlightId(1L);

        Mockito.when(flightService.getById(1L)).thenThrow(new RecordNotFoundException(Flight.class, 1L));

        Assertions.assertThrows(RecordNotFoundException.class, () -> {
            seatService.add(seatCreateRequest);
        });

        Mockito.verify(seatRepository, Mockito.times(0)).save(Mockito.any(Seat.class));
    }

    @Test
    void testUpdate_existingSeat_shouldUpdateSeat() {

        long seatId = 123;
        String passengerName = "John";
        BigDecimal newPrice = new BigDecimal("149.99");

        Seat seat = new Seat();
        seat.setId(seatId);
        seat.setName("A1");
        seat.setPrice(new BigDecimal("129.99"));
        Passenger passenger = new Passenger();
        passenger.setId(1L);
        passenger.setName(passengerName);
        seat.setPassenger(passenger);

        SeatUpdateRequest seatUpdateRequest = new SeatUpdateRequest();
        seatUpdateRequest.setPrice(newPrice);

        Seat expectedSeat = new Seat();
        expectedSeat.setId(seatId);
        expectedSeat.setName("A1");
        expectedSeat.setPrice(newPrice);
        Passenger expectedPassenger = new Passenger();
        expectedPassenger.setId(passenger.getId());
        expectedPassenger.setName(passengerName);
        expectedSeat.setPassenger(expectedPassenger);

        Mockito.when(seatRepository.findById(seatId)).thenReturn(Optional.of(seat));
        Mockito.when(seatRepository.save(Mockito.any())).thenReturn(expectedSeat);

        // act
        Seat actualSeat = seatService.update(seatId, seatUpdateRequest);

        // assert
        Mockito.verify(seatRepository, Mockito.times(1)).findById(seatId);
        Mockito.verify(seatRepository, Mockito.times(1)).save(Mockito.any());
        Assertions.assertEquals(expectedSeat, actualSeat);
    }

    @Test
    void testUpdate_nonExistingSeat_shouldThrowRecordNotFoundException() {

        Mockito.when(seatRepository.findById(1L)).thenReturn(Optional.empty());

        SeatUpdateRequest seatUpdateRequest = new SeatUpdateRequest();
        Assertions.assertThrows(RecordNotFoundException.class, () -> {
            seatService.update(1L, seatUpdateRequest);
        });

        Mockito.verify(seatRepository, Mockito.times(0)).save(Mockito.any(Seat.class));
    }

    @Test
    void testRemove_existingSeat_shouldRemoveSeat() {

        Seat seat = new Seat();
        seat.setId(1L);
        seat.setName("test seat");
        seat.setPrice(new BigDecimal(100));
        seat.setSold(false);

        Mockito.when(seatRepository.findById(1L)).thenReturn(Optional.of(seat));

        seatService.remove(1L);

        Mockito.verify(seatRepository, Mockito.times(1)).delete(seat);
    }

    @Test
    void testRemove_nonExistingSeat_shouldThrowRecordNotFoundException() {

        Mockito.when(seatRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(RecordNotFoundException.class, () -> {
            seatService.remove(1L);
        });

        Mockito.verify(seatRepository, Mockito.times(0)).delete(Mockito.any(Seat.class));
    }

    @Test
    void testGetById_existingSeat_shouldReturnSeat() {

        Seat seat = new Seat();
        seat.setId(1L);
        seat.setName("test seat");
        seat.setPrice(new BigDecimal(100));
        seat.setSold(false);

        Mockito.when(seatRepository.findById(1L)).thenReturn(Optional.of(seat));

        Seat result = seatService.getById(1L);

        Assertions.assertEquals(seat.getId(), result.getId());
        Assertions.assertEquals(seat.getName(), result.getName());
        Assertions.assertEquals(seat.getPrice(), result.getPrice());
        Assertions.assertEquals(seat.isSold(), result.isSold());
    }

    @Test
    void testGetById_nonExistingSeat_shouldThrowRecordNotFoundException() {

        Mockito.when(seatRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(RecordNotFoundException.class, () -> {
            seatService.getById(1L);
        });
    }

    @Test
    void testListByFlightId_availableSeats_shouldReturnAvailableSeats() {

        Seat soldSeat = new Seat();
        soldSeat.setId(1L);
        soldSeat.setName("sold seat");
        soldSeat.setPrice(new BigDecimal(100));
        soldSeat.setSold(true);

        Seat availableSeat = new Seat();
        availableSeat.setId(2L);
        availableSeat.setName("available seat");
        availableSeat.setPrice(new BigDecimal(200));
        availableSeat.setSold(false);

        Mockito.when(seatRepository.findAllByFlightId(1L)).thenReturn(Arrays.asList(soldSeat, availableSeat));

        List<Seat> result = seatService.listByFlightId(1L, true);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(availableSeat.getId(), result.get(0).getId());
        Assertions.assertEquals(availableSeat.getName(), result.get(0).getName());
        Assertions.assertEquals(availableSeat.getPrice(), result.get(0).getPrice());
        Assertions.assertEquals(availableSeat.isSold(), result.get(0).isSold());
    }

    @Test
    void testListByFlightId_allSeats_shouldReturnAllSeats() {

        Seat soldSeat = new Seat();
        soldSeat.setId(1L);
        soldSeat.setName("sold seat");
        soldSeat.setPrice(new BigDecimal(100));
        soldSeat.setSold(true);

        Seat availableSeat = new Seat();
        availableSeat.setId(2L);
        availableSeat.setName("available seat");
        availableSeat.setPrice(new BigDecimal(200));
        availableSeat.setSold(false);

        Mockito.when(seatRepository.findAllByFlightId(1L)).thenReturn(Arrays.asList(soldSeat, availableSeat));

        List<Seat> result = seatService.listByFlightId(1L, null);

        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.contains(soldSeat));
        Assertions.assertTrue(result.contains(availableSeat));
    }

    @Test
    void testGetByIdWithLock_recordNotFound() {
        Long id = 1L;

        Mockito.doReturn(Optional.empty()).when(seatRepository).findSeatByIdWithLock(id);

        Assertions.assertThrows(RecordNotFoundException.class, () -> seatService.getByIdWithLock(id));

        Mockito.verify(seatRepository).findSeatByIdWithLock(id);
    }
}