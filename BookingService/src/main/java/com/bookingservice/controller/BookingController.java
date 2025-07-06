package com.bookingservice.controller;

import com.bookingservice.model.Booking;
import com.bookingservice.model.Bookingdto;
import com.bookingservice.service.BookingService;
import com.flightservice.model.Flight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    BookingService bookingService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Booking createBooking(
            @RequestBody Bookingdto bookingdto) {


        return bookingService.createBooking(bookingdto.getFlightId(), bookingdto.getUserId(), bookingdto.getPassengerCount());
    }

    @GetMapping("/all")
    public List<Flight> getAllFlights() {
        return bookingService.getAllFlights();
    }

    @GetMapping("mybooking/{userId}")
    public List<Booking> getBooking(@PathVariable long userId) {

        return bookingService.getUserBookings(userId);
    }

    @DeleteMapping("/{id}")
    public Booking cancelBooking(
            @PathVariable("id") Long bookingId) {
        return bookingService.cancelBooking(bookingId);

    }

    // Admin endpoints
//    @GetMapping("all")
//    @PreAuthorize("hasRole('ADMIN')")
//    public List<Booking> getAllBookings() {
//        return bookingService.getAllBookings();
//    }
//
//    @GetMapping("/flight/{flightId}")
//    @PreAuthorize("hasRole('ADMIN')")
//    public List<Booking> getFlightBookings(@PathVariable Long flightId) {
//        return bookingService.getBookingsForFlight(flightId);
//}
}

