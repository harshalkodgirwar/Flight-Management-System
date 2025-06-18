package com.flightservice.controller;

import com.flightservice.model.Flight;
import com.flightservice.service.FlightService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/flight")
public class FlightController {

    @Autowired
    private FlightService flightService;

    @GetMapping("/all")
    public ResponseEntity<List<Flight>> getAllFlights() {

        return ResponseEntity.ok(flightService.getAllFlights());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Flight> getFlightById(@PathVariable("id") Long flightId) {
        return flightService.getFlightById(flightId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addFlight(@Valid @RequestBody Flight flight) {
        try {
            Flight savedFlight = flightService.addFlight(flight);
            return new ResponseEntity<>(savedFlight, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{flightId}")
    public ResponseEntity<?> updateFlight(@Valid @RequestBody Flight flight, @PathVariable Long flightId) {
        try{
            Flight updateFlight = flightService.updateFlight(flightId,flight);
            return new ResponseEntity<>(updateFlight, HttpStatus.OK);
        }catch(Exception e)
        {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{flightId}/seats")
    public ResponseEntity<Flight> updateFlightSeats(
            @PathVariable Long flightId,
            @RequestParam Integer seats) {
        Flight updatedFlight = flightService.updateAvailableSeats(flightId, seats);
        return ResponseEntity.ok(updatedFlight);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFlight(@PathVariable("id") Long flightId) {
        try{
            flightService.deleteFlight(flightId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch(Exception e)
        {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}