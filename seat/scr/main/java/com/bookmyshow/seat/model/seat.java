package com.bookmyshow.seat.model;

import com.bookmyshow.booking.models.Booking;
import com.bookmyshow.booking.models.BookingStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "seat")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class seat {

    @Id
    String seatId;

    private SeatStatus status;

    @ManyToOne
    @JoinColumn(name = "locked_by")
    Booking booking;

    @Column(name="locked_at")
    LocalDate lockedAt;
}
