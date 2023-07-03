package com.iyzico.challenge.service;

import com.iyzico.challenge.dto.SeatCreateRequest;
import com.iyzico.challenge.dto.SeatUpdateRequest;
import com.iyzico.challenge.entity.Flight;
import com.iyzico.challenge.entity.Seat;
import com.iyzico.challenge.exception.LockDataException;
import com.iyzico.challenge.exception.RecordNotFoundException;
import com.iyzico.challenge.repository.SeatRepository;
import com.iyzico.challenge.util.ConverterUtil;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SeatService {

    private final SeatRepository seatRepository;
    private final FlightService flightService;

    @PersistenceContext
    private EntityManager entityManager;

    public SeatService(SeatRepository seatRepository, FlightService flightService) {
        this.seatRepository = seatRepository;
        this.flightService = flightService;
    }

    public Seat save(Seat seat) {
        return seatRepository.save(seat);
    }

    public Seat add(SeatCreateRequest seatCreateRequest) {
        Flight flight = flightService.getById(seatCreateRequest.getFlightId());

        Seat createReq = ConverterUtil.toSeatEntity(seatCreateRequest);
        createReq.setFlight(flight);

        flightService.save(flight);

        return seatRepository.save(createReq);
    }

    public Seat update(Long id, SeatUpdateRequest seatUpdateRequest) {
        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(Seat.class, id));

        Seat updateReq = ConverterUtil.toSeatEntity(seat, seatUpdateRequest);

        return seatRepository.save(updateReq);
    }

    public void remove(Long id) {
        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(Seat.class, id));

        seatRepository.delete(seat);
    }

    public Seat getById(Long id) {

        return seatRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(Seat.class, id));
    }

    @Transactional
    public Seat getByIdWithLock(Long id) {
        try {
            Seat seat = seatRepository.findSeatByIdWithLock(id)
                    .orElseThrow(() -> new RecordNotFoundException(Seat.class, id));

            entityManager.refresh(seat);

            return seat;

        } catch (PessimisticLockingFailureException e) {
            throw new LockDataException(Seat.class, id);
        }
    }

    public List<Seat> listByFlightId(Long id, Boolean available) {

        List<Seat> allByFlightId = seatRepository.findAllByFlightId(id);

        if (available != null)
            return allByFlightId.stream()
                    .filter(seat -> seat.isSold() != available)
                    .collect(Collectors.toList());

        return allByFlightId;
    }

}
