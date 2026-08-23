package com.bookmyshow.common.models;

import com.bookmyshow.common.enums.SeatStatus;
import com.bookmyshow.common.id.ShowSeatId;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "show_seat")
@IdClass(ShowSeatId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShowSeat {

    @Id
    @Column(name = "seat_id",length = 50)
    private String seatId;

    @Id
    @Column(name = "show_id",length = 50)
    private String showId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SeatStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locked_by")
    private Booking booking;

    @Column(name = "locked_at")
    private LocalDateTime lockedAt;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    @Version
    private Long version;
}
