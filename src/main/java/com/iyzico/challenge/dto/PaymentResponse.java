package com.iyzico.challenge.dto;

public class PaymentResponse {

    private String passengerName;

    private Long flightId;

    private String flightName;

    private Long selectedSeatId;

    private String selectedSeatName;


    public PaymentResponse(String passengerName, Long flightId, String flightName, Long selectedSeatId, String selectedSeatName) {
        this.passengerName = passengerName;
        this.flightId = flightId;
        this.flightName = flightName;
        this.selectedSeatId = selectedSeatId;
        this.selectedSeatName = selectedSeatName;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public Long getFlightId() {
        return flightId;
    }

    public void setFlightId(Long flightId) {
        this.flightId = flightId;
    }

    public String getFlightName() {
        return flightName;
    }

    public void setFlightName(String flightName) {
        this.flightName = flightName;
    }

    public Long getSelectedSeatId() {
        return selectedSeatId;
    }

    public void setSelectedSeatId(Long selectedSeatId) {
        this.selectedSeatId = selectedSeatId;
    }

    public String getSelectedSeatName() {
        return selectedSeatName;
    }

    public void setSelectedSeatName(String selectedSeatName) {
        this.selectedSeatName = selectedSeatName;
    }
}
