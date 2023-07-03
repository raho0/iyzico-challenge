package com.iyzico.challenge.service;

import com.iyzico.challenge.dto.FlightCreateRequest;
import com.iyzico.challenge.dto.FlightUpdateRequest;
import com.iyzico.challenge.entity.Flight;
import com.iyzico.challenge.entity.Seat;
import com.iyzico.challenge.exception.RecordNotFoundException;
import com.iyzico.challenge.repository.FlightRepository;
import com.iyzico.challenge.util.ConverterUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlightService {

    private final FlightRepository flightRepository;

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }


    public Flight save(Flight flight) {
        return flightRepository.save(flight);
    }

    public Flight add(FlightCreateRequest flightCreateRequest) {
        Flight createReq = ConverterUtil.toFlightEntity(flightCreateRequest);

        return flightRepository.save(createReq);
    }

    public Flight update(Long id, FlightUpdateRequest flightUpdateRequest) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(Flight.class, id));

        Flight updateReq = ConverterUtil.toFlightEntity(flight, flightUpdateRequest);

        return flightRepository.save(updateReq);
    }

    public void remove(Long id) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(Flight.class, id));

        flightRepository.delete(flight);
    }

    public Flight getById(Long id) {

        return flightRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(Flight.class, id));
    }

    public Flight listById(Long id) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(Flight.class, id));

        List<Seat> availableSeats = flight.getSeats().stream()
                .filter(seat -> !seat.isSold())
                .collect(Collectors.toList());

        flight.setSeats(availableSeats);

        return flight;
    }
}
