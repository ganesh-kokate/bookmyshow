package com.bookmyshow.seat.repository;

import com.bookmyshow.common.models.Seat;
import com.bookmyshow.common.models.SeatStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Seat s WHERE s.seatId IN :ids")
    List<Seat> findSeatsForBooking(@Param("ids") List<String> ids);


    @Modifying
    @Query("""
        UPDATE Seat s
        SET s.status = :available,
            s.lockedAt = null,
            s.booking = null
        WHERE s.status = :locked
        AND s.lockedAt < :expiryTime
    """)
    int releaseExpiredSeats(
            @Param("locked") SeatStatus locked,
            @Param("available") SeatStatus available,
            @Param("expiryTime") LocalDateTime expiryTime
    );
}
