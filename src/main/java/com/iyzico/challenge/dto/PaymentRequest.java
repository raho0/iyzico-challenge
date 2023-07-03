package com.iyzico.challenge.dto;

public class PaymentRequest {

    private Long flightId;
    private String passengerName;
    private Long selectedSeatId;


    public Long getFlightId() {
        return flightId;
    }

    public void setFlightId(Long flightId) {
        this.flightId = flightId;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public Long getSelectedSeatId() {
        return selectedSeatId;
    }

    public void setSelectedSeatId(Long selectedSeatId) {
        this.selectedSeatId = selectedSeatId;
    }
}
