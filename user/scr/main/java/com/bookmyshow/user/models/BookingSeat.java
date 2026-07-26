package com.bookmyshow.user.models;

import com.bookmyshow.booking.models.Booking;
import com.bookmyshow.seat.model.seat;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class BookingSeat {

    @Id
    private String bookingSeatId;

    @ManyToOne
    private Booking booking;

    @ManyToOne
    private seat seat;

}