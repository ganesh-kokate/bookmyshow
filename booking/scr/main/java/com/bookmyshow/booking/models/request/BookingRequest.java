package com.bookmyshow.booking.models.request;

import java.util.List;

public record BookingRequest( Long eventId,
                              String userId,
                              List<String> seatIds) {

}
