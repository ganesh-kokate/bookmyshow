package com.bookmyshow.seat.service;

import com.bookmyshow.common.enums.SeatStatus;
import com.bookmyshow.common.models.Booking;
import com.bookmyshow.common.models.ShowSeat;
import com.bookmyshow.common.repository.BookingRepository;
import com.bookmyshow.common.repository.SeatRepository;
import com.bookmyshow.seat.model.request.LockSeatsRequest;
import com.bookmyshow.seat.model.response.ConfirmSeatResponse;
import com.bookmyshow.seat.model.response.LockSeatsResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class SeatService {

    private final SeatRepository seatRepository;
    private final SeatLockService seatLockService;
    private final BookingRepository bookingRepository;
   public List<Integer> getAvailableSeats()
   {
       return null;
   }

   @Transactional
   public LockSeatsResponse lockSeats(LockSeatsRequest lockSeatsRequest, String userId) {
       String showId = lockSeatsRequest.getShowId();
       List<String> seatIds = lockSeatsRequest.getSeatIds();
       List<ShowSeat> seats = seatRepository.findSeatsForBooking(seatIds, showId);

       for (ShowSeat seat : seats) {
           if (seat.getStatus() == SeatStatus.booked) {
               throw new RuntimeException("Seat already booked: " + seat.getSeatId());
           }
       }
       List<String> acquiredSeats = new ArrayList<>();
       try {
           for (String seatId : seatIds) {
               boolean locked = seatLockService.tryLock(seatId, showId, userId);
               if (!locked) {
                   log.info("Seats {} is alredy Locked ", lockSeatsRequest.getSeatIds());
                   throw new RuntimeException("Seat is currently locked: " + seatId
                   );
               }
               acquiredSeats.add(seatId);
           }

           LockSeatsResponse response = new LockSeatsResponse();
           response.setSeatIds(seatIds);
           response.setLockedAt(LocalDateTime.now());
           return response;

       } catch (RuntimeException ex) {
           for (String seatId : acquiredSeats) {
               seatLockService.releaseLock(seatId, showId, userId);
           }
           throw ex;
       }
   }



    public ConfirmSeatResponse bookSeats(String bookingId)
    {
        Booking booking = bookingRepository.findByIdWithSeats(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found: " + bookingId));

        List<ShowSeat> seats = booking.getShowSeats();
        ConfirmSeatResponse confirmSeatResponse = new ConfirmSeatResponse();
        LocalDateTime bookedAt = LocalDateTime.now();
        for (ShowSeat seat : seats) {

          if(seatLockService.validateLock(seat.getSeatId(),seat.getShowId(),booking.getUser().getUserId()))
          {
              seat.setStatus(SeatStatus.booked);
              seat.setLockedAt(bookedAt);
              seatRepository.save(seat);
          }
          else{
              log.info("seat is acquired by someone else .... Try another seat");
              throw new RuntimeException("seat is acquired by someone else: " + seat.getSeatId()
              );
          }
        }
        List<String> seatIds = seats.stream()
                .map(ShowSeat::getSeatId)
                .toList();

        confirmSeatResponse.setSeatIds(seatIds);
        confirmSeatResponse.setStatus(SeatStatus.booked);
        confirmSeatResponse.setBookedAt(bookedAt);
        return confirmSeatResponse;
    }

}
