package com.iyzico.challenge.controller;

import com.iyzico.challenge.dto.FlightCreateRequest;
import com.iyzico.challenge.dto.FlightUpdateRequest;
import com.iyzico.challenge.entity.Flight;
import com.iyzico.challenge.service.FlightService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/flight")
public class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @PostMapping()
    public ResponseEntity<Flight> create(
            @RequestBody FlightCreateRequest createRequest) {

        return new ResponseEntity<>(flightService.add(createRequest), HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Flight> update(
            @PathVariable(value = "id") Long id,
            @RequestBody FlightUpdateRequest updateRequest) {

        return new ResponseEntity<>(flightService.update(id, updateRequest), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable(value = "id") Long id) {

        flightService.remove(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Flight> getById(
            @PathVariable(value = "id") Long id) {

        return new ResponseEntity<>(flightService.getById(id), HttpStatus.OK);
    }

    @GetMapping(value = "/list/{id}")
    public ResponseEntity<Flight> listById(
            @PathVariable(value = "id") Long id) {

        return new ResponseEntity<>(flightService.listById(id), HttpStatus.OK);
    }

}
