package com.bookmyshow.booking.service;

import com.bookmyshow.booking.models.request.BookingRequest;
import com.bookmyshow.booking.repository.BookkingRepository;
import com.bookmyshow.common.models.Booking;
import com.bookmyshow.common.models.BookingStatus;
import com.bookmyshow.common.models.SeatStatus;
import com.bookmyshow.common.models.User;
import com.bookmyshow.seat.service.SeatService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class BookingService {

    private final BookkingRepository bookingRepository;
    private final SeatService seatService;

    private static final String SEAT_SERVICE_LOCK_URL = "http://localhost:8082/api/seats/acquire-lock";

    @Transactional
    public String createBooking(BookingRequest bookingRequest) {

        Map<String, Object> lockSeatsRequest = new HashMap<>();
        lockSeatsRequest.put("seatIds", bookingRequest.seatIds());

        seatService.lockSeats(bookingRequest.seatIds());
        // Step 3: Create and persist the Booking entity with PENDING status
        String bookingId = UUID.randomUUID().toString();
        // Optional: Reference User entity if user exists
        User user = new User();
        user.setUserId(bookingRequest.userId());
        Booking booking = Booking.builder()
                .bookingId(bookingId)
                .user(user)
                .status(BookingStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();
        bookingRepository.save(booking);
        log.info("Booking created successfully with ID: {}", bookingId);
        return bookingId;
    }


    // TODO : will be used for when user pay
    public String confirmBooking()
    {
        return "Success";
    }

    // TODO : will be used for when user cancel booking
    public String cancelBooking()
    {
        return "Success";
    }


}
