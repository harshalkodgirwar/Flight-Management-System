package com.flightservice.exceptions;

public class SeatUpdateException extends RuntimeException {
    public SeatUpdateException(String message) {
        super(message);
    }
}