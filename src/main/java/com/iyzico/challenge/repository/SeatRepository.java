package com.iyzico.challenge.repository;

import com.iyzico.challenge.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT seat FROM Seat seat WHERE seat.id = :id")
    Optional<Seat> findSeatByIdWithLock(@Param("id") Long id);

    List<Seat> findAllByFlightId(Long flightId);
}
