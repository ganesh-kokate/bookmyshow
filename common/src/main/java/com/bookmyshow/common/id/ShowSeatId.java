package com.bookmyshow.common.id;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ShowSeatId implements Serializable {
    private String seatId;
    private String showId;
}
