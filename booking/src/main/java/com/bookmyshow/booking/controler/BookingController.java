package com.bookmyshow.booking.controler;

import com.bookmyshow.booking.models.request.BookingRequest;
import com.bookmyshow.booking.models.request.ConfirmRequest;
import com.bookmyshow.booking.service.BookingService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/booking")
@AllArgsConstructor
public class BookingController {

    private final BookingService bookingservice;

    @PostMapping("/bookseats")
    public String book(@RequestBody BookingRequest bookingRequest)
    {
       return bookingservice.createBooking(bookingRequest);
    }

    @PostMapping("/cofirm")
    public String confirm(@RequestBody ConfirmRequest confirmRequest)
    {
        bookingservice.confirmBooking();
        return null;
    }

    @PostMapping("/cancel")
    public void cancle(@PathVariable String bookingId)
    {
        bookingservice.cancelBooking();
    }



}
