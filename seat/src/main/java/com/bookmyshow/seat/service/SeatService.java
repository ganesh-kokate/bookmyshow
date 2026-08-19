package com.bookmyshow.seat.service;

import com.bookmyshow.common.models.Seat;
import com.bookmyshow.common.models.SeatStatus;
import com.bookmyshow.seat.model.request.LockSeatsRequest;
import com.bookmyshow.seat.model.response.LockSeatsResponse;
import com.bookmyshow.seat.repository.SeatRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class SeatService {

    private final SeatRepository seatRepository;
   public List<Integer> getAvailableSeats()
   {
       return null;
   }

   @Transactional
   public LockSeatsResponse lockSeats(LockSeatsRequest lockSeatsRequest)
   {
       List<Seat> seats = seatRepository.findSeatsForBooking(lockSeatsRequest.getSeatIds());
       LockSeatsResponse lockSeatsResponse = new LockSeatsResponse();
       LocalDateTime lockedAt = LocalDateTime.now();
       for (Seat seat : seats) {

           if (seat.getStatus() == SeatStatus.booked) {
               log.info("Seats {} is alredy Booked ", lockSeatsRequest.getSeatIds());
               throw new RuntimeException("Seat already booked: " + seat.getSeatId()
               );
           }

           if (seat.getStatus() == SeatStatus.locked) {
               log.info("Seats {} is alredy Locked ", lockSeatsRequest.getSeatIds());
               throw new RuntimeException("Seat is currently locked: " + seat.getSeatId()
               );
           }

           seat.setStatus(SeatStatus.locked);
           seat.setLockedAt(lockedAt);
           seatRepository.save(seat);
       }
       List<String> seatIds = seats.stream()
               .map(Seat::getSeatId)
               .toList();

       lockSeatsResponse.setSeatIds(seatIds);
       lockSeatsResponse.setStatus(SeatStatus.locked);
       lockSeatsResponse.setLockedAt(lockedAt);
       return lockSeatsResponse;
   }

    @Scheduled(fixedRate = 30000)
    @Transactional
    public void  releaseSeats()
    {
        LocalDateTime expiryTime = LocalDateTime.now().minusMinutes(5);
        int released = seatRepository.releaseExpiredSeats(
                SeatStatus.locked,
                SeatStatus.available,
                expiryTime
        );
        log.info("Released Seats{}", released);
    }

    public void bookSeats()
    {

    }

    public void isAvailable()
    {

    }

    public void isNotLocked()
    {

    }

}
