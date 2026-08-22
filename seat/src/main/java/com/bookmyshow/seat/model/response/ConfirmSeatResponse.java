package com.bookmyshow.seat.model.response;

import com.bookmyshow.common.models.SeatStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ConfirmSeatResponse {
    private List<String> seatIds;
    private SeatStatus status;
    private LocalDateTime bookedAt;
}
