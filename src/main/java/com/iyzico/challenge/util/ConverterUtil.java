package com.iyzico.challenge.util;

import com.iyzico.challenge.dto.FlightCreateRequest;
import com.iyzico.challenge.dto.FlightUpdateRequest;
import com.iyzico.challenge.dto.SeatCreateRequest;
import com.iyzico.challenge.dto.SeatUpdateRequest;
import com.iyzico.challenge.entity.Flight;
import com.iyzico.challenge.entity.Seat;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class ConverterUtil {

    private ConverterUtil() {
        throw new IllegalStateException("Util class");
    }

    public static Flight toFlightEntity(FlightCreateRequest flightCreateRequest) {
        Flight flight = new Flight();

        flight.setName(flightCreateRequest.getName());
        flight.setDescription(flightCreateRequest.getDescription());

        List<Seat> seatList = flightCreateRequest.getSeats().stream()
                .map(ConverterUtil::toSeatEntity).collect(Collectors.toList());

        seatList.forEach(seat -> seat.setFlight(flight));

        flight.setSeats(seatList);

        return flight;
    }

    public static Flight toFlightEntity(Flight flight, FlightUpdateRequest flightUpdateRequest) {

        if (flightUpdateRequest.getName() != null && !flightUpdateRequest.getName().isEmpty())
            flight.setName(flightUpdateRequest.getName());

        if (flightUpdateRequest.getDescription() != null && !flightUpdateRequest.getDescription().isEmpty())
            flight.setDescription(flightUpdateRequest.getDescription());

        return flight;
    }

    public static Seat toSeatEntity(SeatCreateRequest seatCreateRequest) {
        Seat seat = new Seat();

        seat.setName(seatCreateRequest.getName());

        if (seatCreateRequest.getPrice() == null || seatCreateRequest.getPrice().compareTo(BigDecimal.ZERO) <= 0)
            throw new RuntimeException();

        seat.setPrice(seatCreateRequest.getPrice());
        seat.setSold(false);

        return seat;
    }

    public static Seat toSeatEntity(Seat seat, SeatUpdateRequest seatUpdateRequest) {

        if (seatUpdateRequest.getName() != null && !seatUpdateRequest.getName().isEmpty())
            seat.setName(seatUpdateRequest.getName());

        if (seatUpdateRequest.getSold() != null)
            seat.setSold(seatUpdateRequest.getSold());

        if (seatUpdateRequest.getPassenger() != null)
            seat.setPassenger(seatUpdateRequest.getPassenger());

        if (seatUpdateRequest.getPrice() != null)
            seat.setPrice(seatUpdateRequest.getPrice());

        return seat;
    }
}
