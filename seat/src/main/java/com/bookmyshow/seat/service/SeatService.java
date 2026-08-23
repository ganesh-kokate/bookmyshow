package com.bookmyshow.seat.service;

import com.bookmyshow.common.enums.SeatStatus;
import com.bookmyshow.common.models.ShowSeat;
import com.bookmyshow.seat.model.request.LockSeatsRequest;
import com.bookmyshow.seat.model.response.ConfirmSeatResponse;
import com.bookmyshow.seat.model.response.LockSeatsResponse;
import com.bookmyshow.seat.repository.SeatRepository;
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
   public List<Integer> getAvailableSeats()
   {
       return null;
   }

   @Transactional
   public LockSeatsResponse lockSeats(LockSeatsRequest lockSeatsRequest)
   {
       List<ShowSeat> seats = seatRepository.findSeatsForBooking(lockSeatsRequest.getSeatIds(),lockSeatsRequest.getShowId());
       LockSeatsResponse lockSeatsResponse = new LockSeatsResponse();
       LocalDateTime lockedAt = LocalDateTime.now();
       String lockToken = UUID.randomUUID().toString();
       List<String> acquiredSeats = new ArrayList<>();
       try {
           for (ShowSeat seat : seats) {
               boolean locked = seatLockService.tryLock(seat.getSeatId(),lockSeatsRequest.getShowId(), lockToken);

               if (!locked) {
                   log.info("Seats {} is alredy Locked ", lockSeatsRequest.getSeatIds());
                   throw new RuntimeException("Seat is currently locked: " + seat.getSeatId()
                   );
               }

               if (seat.getStatus() == SeatStatus.booked) {
                   throw new RuntimeException(
                           "Seat already booked: " + seat.getSeatId()
                   );
               }

               acquiredSeats.add(seat.getSeatId());
              // seat.setStatus(SeatStatus.locked);
               seat.setLockedAt(lockedAt);
               seatRepository.save(seat);
               bookSeats(lockSeatsRequest);
           }

           lockSeatsResponse.setSeatIds(lockSeatsRequest.getSeatIds());
           //lockSeatsResponse.setStatus(SeatStatus.locked);
           lockSeatsResponse.setLockedAt(lockedAt);
           return lockSeatsResponse;
       }
       catch (RuntimeException ex)
       {
           for (String seatId : acquiredSeats) {
               seatLockService.releaseLock(seatId,lockSeatsRequest.getShowId(), lockToken);
           }

           throw ex;
       }
   }

//    @Scheduled(fixedRate = 30000)
//    @Transactional
//    public void  releaseSeats()
//    {
//        LocalDateTime expiryTime = LocalDateTime.now().minusMinutes(5);
//        int released = seatRepository.releaseExpiredSeats(
//                SeatStatus.locked,
//                SeatStatus.available,
//                expiryTime
//        );
//        log.info("Released Seats{}", released);
//    }

    public ConfirmSeatResponse bookSeats(LockSeatsRequest confirmRequest)
    {
        List<ShowSeat> seats = seatRepository.findSeatsForBooking(confirmRequest.getSeatIds(),confirmRequest.getShowId());
        ConfirmSeatResponse confirmSeatResponse = new ConfirmSeatResponse();
        LocalDateTime bookedAt = LocalDateTime.now();
        for (ShowSeat seat : seats) {

            if (seat.getStatus() == SeatStatus.booked) {
                log.info("Seats {} is alredy Booked ", confirmRequest.getSeatIds());
                throw new RuntimeException("Seat already booked: " + seat.getSeatId()
                );
            }

            seat.setStatus(SeatStatus.booked);
            seat.setLockedAt(bookedAt);
            seatRepository.save(seat);
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
