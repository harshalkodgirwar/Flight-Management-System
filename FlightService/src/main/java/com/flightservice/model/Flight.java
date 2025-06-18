package com.flightservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "flight")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flightId;

    @NotBlank
    @Column(unique = true)
    private String flightNumber;

    @NotBlank
    private String airlineName;

    @NotBlank
    private String source;

    @NotBlank
    private String destination;

    @PositiveOrZero
    private Integer seatsAvailable;

    @Future
    private LocalDateTime departureTime;

    @Future
    private LocalDateTime arrivalTime;

    @Positive
    private Double price;

    @Transient
    public String getDuration() {
        if (departureTime != null && arrivalTime != null) {
            long hours = java.time.Duration.between(departureTime, arrivalTime).toHours();
            long minutes = java.time.Duration.between(departureTime, arrivalTime).toMinutesPart();
            return String.format("%dh %02dm", hours, minutes);
        }
        return null;
    }

    public @NotBlank String getAirlineName() {
        return airlineName;
    }

    public void setAirlineName(@NotBlank String airlineName) {
        this.airlineName = airlineName;
    }

    public @Future LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(@Future LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public @Future LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(@Future LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public @NotBlank String getDestination() {
        return destination;
    }

    public void setDestination(@NotBlank String destination) {
        this.destination = destination;
    }

    public Long getFlightId() {
        return flightId;
    }

    public void setFlightId(Long flightId) {
        this.flightId = flightId;
    }

    public @NotBlank String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(@NotBlank String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public @Positive Double getPrice() {
        return price;
    }

    public void setPrice(@Positive Double price) {
        this.price = price;
    }

    public @PositiveOrZero Integer getSeatsAvailable() {
        return seatsAvailable;
    }

    public void setSeatsAvailable(@PositiveOrZero Integer seatsAvailable) {
        this.seatsAvailable = seatsAvailable;
    }

    public @NotBlank String getSource() {
        return source;
    }

    public void setSource(@NotBlank String source) {
        this.source = source;
    }
}