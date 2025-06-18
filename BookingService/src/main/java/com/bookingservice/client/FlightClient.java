package com.bookingservice.client;

import com.flightservice.model.Flight;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "FLIGHTSERVICE")
public interface FlightClient {

    @GetMapping("/flight/{id}")
    Flight getFlightById(@PathVariable("id") Long FlightId);

    @PutMapping("/flight/{id}/seats")
    void updateSeats(@PathVariable("id") Long FlightId, @RequestParam Integer seats);

    @GetMapping("/flight/all")
    List<Flight> getAllFlights();
}
