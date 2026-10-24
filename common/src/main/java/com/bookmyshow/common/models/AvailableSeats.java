package com.bookmyshow.common.models;

import java.util.List;

public record AvailableSeats(
        String showId,
        List<AvailableSeatResponceForScreen> seats
) {
}
