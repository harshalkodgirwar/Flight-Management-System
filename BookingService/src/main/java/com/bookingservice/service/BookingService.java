package com.bookingservice.service;

import com.bookingservice.client.FlightClient;
import com.bookingservice.model.Booking;
import com.bookingservice.model.BookingStatus;
import com.bookingservice.repository.BookingRepo;
import com.flightservice.exceptions.BookingException;
import com.flightservice.model.Flight;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    BookingRepo repo;

    @Autowired
    private FlightClient flightClient;

    @Transactional
    public Booking createBooking(Long flightId, Long userId, Integer passengers) {

        // 1. verify if flight exist and has seat available
        Flight flight = flightClient.getFlightById(flightId);

        if(flight.getSeatsAvailable() < passengers)
        {
            throw new BookingException("Not enough seats available");
        }

        //2. create booking
        Booking booking = new Booking();

        booking.setFlightId(flightId);
        booking.setUserId(userId);
        booking.setPassengerCount(passengers);
        booking.setTotalPrice(flight.getPrice() * passengers);
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setBookingDate(LocalDateTime.now());
        booking.setFlightNumber(flight.getFlightNumber());
        booking.setAirline(flight.getAirlineName());
        booking.setDepartureTime(flight.getDepartureTime());

        //3. Update flight inventory
        flightClient.updateSeats(flightId, flight.getSeatsAvailable() - passengers);

        return repo.save(booking);
    }


    public List<Booking> getUserBookings(Long userId) {
        return repo.findByUserId(userId);
    }

    @Transactional
    public Booking cancelBooking(Long bookingId) {
        Booking booking = repo.findById(bookingId)
                .orElseThrow(() -> new BookingException("Booking not found"));

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BookingException("Booking already cancelled");
        }

        // Return seats to inventory
        flightClient.updateSeats(booking.getFlightId(),
                flightClient.getFlightById(booking.getFlightId()).getSeatsAvailable() +
                        booking.getPassengerCount());

        booking.setStatus(BookingStatus.CANCELLED);
        return repo.save(booking);

    }

    // Admin operations
    public List<Booking> getAllBookings() {
        return repo.findAll();
    }

    public List<Booking> getBookingsForFlight(Long flightId) {
        return repo.findByFlightId(flightId);
    }

    public List<Flight> getAllFlights() {
        return flightClient.getAllFlights();
    }
}
