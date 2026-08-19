package com.bookmyshow.booking.models.request;

public record ConfirmRequest(
        String bookingId,
        String amount,
        String userId
) {
}
