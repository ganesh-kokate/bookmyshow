package com.bookmyshow.common.models;

import com.bookmyshow.common.enums.SeatType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "seat")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seat {

    @Id
    @Column(name = "seat_id", length = 50)
    private String seatId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "theotorid", referencedColumnName = "theotorid"),
            @JoinColumn(name = "screen_name", referencedColumnName = "screen_name")
    })
    private Screen screen;

    @Enumerated(EnumType.STRING)
    @Column(name = "seattype")
    private SeatType type;

}
