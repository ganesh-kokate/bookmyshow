package com.bookmyshow.seat.controler;

import com.bookmyshow.seat.model.request.LockSeatsRequest;
import com.bookmyshow.seat.model.response.LockSeatsResponse;
import com.bookmyshow.seat.service.SeatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/seats")
@RequiredArgsConstructor
@Log4j2
public class SeatControler {

    private final SeatService seatService;

    public List<Integer> getSeats()
    {
        return null;
    }

    @PostMapping("/acquire-lock")
    public LockSeatsResponse lockSeats( @RequestBody LockSeatsRequest lockSeatsRequest)
    {

        log.info("Acqiring lock on seat ids {}", lockSeatsRequest.getSeatIds());
        return seatService.lockSeats(lockSeatsRequest);
    }
}
