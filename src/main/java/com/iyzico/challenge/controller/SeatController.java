package com.iyzico.challenge.controller;

import com.iyzico.challenge.dto.SeatCreateRequest;
import com.iyzico.challenge.dto.SeatUpdateRequest;
import com.iyzico.challenge.entity.Seat;
import com.iyzico.challenge.service.SeatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seat")
public class SeatController {

    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }


    @PostMapping()
    public ResponseEntity<Seat> create(
            @RequestBody SeatCreateRequest createRequest) {

        return new ResponseEntity<>(seatService.add(createRequest), HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Seat> update(
            @PathVariable(value = "id") Long id,
            @RequestBody SeatUpdateRequest updateRequest) {

        return new ResponseEntity<>(seatService.update(id, updateRequest), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable(value = "id") Long id) {

        seatService.remove(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Seat> getById(
            @PathVariable(value = "id") Long id) {

        return new ResponseEntity<>(seatService.getById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Seat>> getAllByFlightId(
            @RequestParam(value = "flightId") Long id,
            @RequestParam(value = "available", required = false) Boolean available) {

        return new ResponseEntity<>(seatService.listByFlightId(id, available), HttpStatus.OK);
    }

}
