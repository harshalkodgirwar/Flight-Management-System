package com.flightservice.service;

import com.flightservice.exceptions.FlightNotFoundException;
import com.flightservice.exceptions.SeatUpdateException;
import com.flightservice.model.Flight;
import com.flightservice.repository.FlightRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FlightService {

    private final FlightRepo flightRepo;

    @Autowired
    public FlightService(FlightRepo flightRepo) {
        this.flightRepo = flightRepo;
    }

    public List<Flight> getAllFlights() {
        return flightRepo.findAll();
    }

    public Optional<Flight> getFlightById(Long flightId) {
        return flightRepo.findById(flightId);
    }

    public Flight addFlight(Flight flight) {
        // Validate required fields before save
        if (flight.getFlightNumber() == null || flight.getAirlineName() == null) {
            throw new IllegalArgumentException("Flight number and airline name are required");
        }
        return flightRepo.save(flight);
    }

    @Transactional
    public Flight updateFlight(Long flightId, Flight flightDetails) {
        Flight flight = flightRepo.findById(flightId)
                .orElseThrow(() -> new RuntimeException("Flight not found with id: " + flightId));

        // Update only non-null fields
        if (flightDetails.getFlightNumber() != null) {
            flight.setFlightNumber(flightDetails.getFlightNumber());
        }
        if (flightDetails.getAirlineName() != null) {
            flight.setAirlineName(flightDetails.getAirlineName());
        }
        if (flightDetails.getSource() != null) {
            flight.setSource(flightDetails.getSource());
        }
        if (flightDetails.getDestination() != null) {
            flight.setDestination(flightDetails.getDestination());
        }
        if (flightDetails.getSeatsAvailable() != 0) {
            flight.setSeatsAvailable(flightDetails.getSeatsAvailable());
        }
        if (flightDetails.getDepartureTime() != null) {
            flight.setDepartureTime(flightDetails.getDepartureTime());
        }
        if (flightDetails.getArrivalTime() != null) {
            flight.setArrivalTime(flightDetails.getArrivalTime());
        }
        if (flightDetails.getPrice() != 0) {
            flight.setPrice(flightDetails.getPrice());
        }

        return flightRepo.save(flight);
    }

    public Flight updateAvailableSeats(Long flightId, Integer seatChange) {
        Flight flight = flightRepo.findById(flightId)
                .orElseThrow(() -> new FlightNotFoundException("Flight not found with id: " + flightId));

        int newSeatCount = flight.getSeatsAvailable() + seatChange;

        if (newSeatCount < 0) {
            throw new SeatUpdateException("Cannot reduce seats below zero");
        }

        flight.setSeatsAvailable(newSeatCount);
        return flightRepo.save(flight);
    }

    public void deleteFlight(Long flightId) {

        flightRepo.deleteById(flightId);
    }
}