package com.bookmyshow.common.models;

import java.util.Map;

public record AllotSeatRequest(
        String showId,
        Map<SeatType,SeatRange> seats
) {
}


