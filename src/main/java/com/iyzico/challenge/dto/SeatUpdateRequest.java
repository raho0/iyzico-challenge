package com.iyzico.challenge.dto;

import com.iyzico.challenge.entity.Passenger;

import java.math.BigDecimal;

public class SeatUpdateRequest {

    private Long id;

    private String name;

    private Boolean sold;

    private BigDecimal price;

    private Passenger passenger;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getSold() {
        return sold;
    }

    public void setSold(Boolean sold) {
        this.sold = sold;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }
}
