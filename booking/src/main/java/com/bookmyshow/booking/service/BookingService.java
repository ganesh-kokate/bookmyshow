package com.bookmyshow.booking.service;

import com.bookmyshow.booking.models.request.BookingRequest;
import com.bookmyshow.common.enums.SeatStatus;
import com.bookmyshow.common.models.*;
import com.bookmyshow.common.repository.BookingRepository;
import com.bookmyshow.common.enums.BookingStatus;
import com.bookmyshow.common.repository.SeatRepository;
import com.bookmyshow.seat.model.response.ConfirmSeatResponse;
import com.bookmyshow.seat.service.SeatService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class BookingService {

    private final BookingRepository bookingRepository;
    private final SeatService seatService;
    private final SeatRepository seatRepository;

    @Transactional
    public String createBooking(BookingRequest bookingRequest) {

        String showId = bookingRequest.lockSeatsRequest().getShowId();
        List<String> seatIds = bookingRequest.lockSeatsRequest().getSeatIds();
        List<ShowSeat> seats = seatRepository.findSeatsForBooking(seatIds, showId);

       seatService.lockSeats(bookingRequest.lockSeatsRequest(),bookingRequest.userId());
       String bookingId = UUID.randomUUID().toString();

        User user = new User();
        user.setUserId(bookingRequest.userId());

        Shows show = Shows.builder()
                .showId(showId)
                .build();

        Booking booking = Booking.builder()
                .bookingId(bookingId)
                .user(user)
                .status(BookingStatus.PENDING)
                .show(show)
                .createdAt(LocalDateTime.now())
                .build();

        booking=bookingRepository.save(booking);

        LocalDateTime now = LocalDateTime.now();
        for (ShowSeat seat : seats) {
            seat.setBooking(booking);
            seat.setStatus(SeatStatus.locked);
            seat.setLockedAt(now);
        }

        seatRepository.saveAll(seats);

        log.info("Booking created successfully with ID: {}", bookingId);
        return bookingId;
    }


    public ConfirmSeatResponse confirmBooking(String bookingId)
    {
        Booking booking = bookingRepository.getReferenceById(bookingId);
        ConfirmSeatResponse confirmSeatResponse = seatService.bookSeats(bookingId);
        booking.setStatus(BookingStatus.CONFIRMED);
        bookingRepository.save(booking);
        return confirmSeatResponse;
    }

    public String cancelBooking(String bookingId)
    {
        Booking booking = bookingRepository.getReferenceById(bookingId);
        List<ShowSeat> seats= booking.getShowSeats();

        for (int i=0;i<seats.size();i++)
        {
            seats.get(i).setStatus(SeatStatus.available);
            seatRepository.save(seats.get(i));
        }
        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
        return bookingId;
    }


}
