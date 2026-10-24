package com.bookmyshow.common.models;

import com.bookmyshow.common.enums.SeatStatus;

import java.math.BigDecimal;

public record AvailableSeatResponceForScreen(
        String seatId,
        SeatStatus status,
        BigDecimal price
) {
}
