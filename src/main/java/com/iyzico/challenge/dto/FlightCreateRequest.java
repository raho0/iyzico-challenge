package com.iyzico.challenge.dto;

import java.util.List;

public class FlightCreateRequest {

    private String name;

    private String description;

    private List<SeatCreateRequest> seats;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<SeatCreateRequest> getSeats() {
        return seats;
    }

    public void setSeats(List<SeatCreateRequest> seat) {
        this.seats = seat;
    }

}
