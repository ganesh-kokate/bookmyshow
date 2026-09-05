package com.bookmyshow.booking.models.request;

import com.bookmyshow.seat.model.request.LockSeatsRequest;

import java.util.List;

public record BookingRequest( String userId,
                              LockSeatsRequest lockSeatsRequest) {

}
