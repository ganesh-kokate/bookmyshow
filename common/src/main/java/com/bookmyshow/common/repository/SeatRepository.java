package com.bookmyshow.common.repository;

import com.bookmyshow.common.enums.SeatStatus;
import com.bookmyshow.common.models.ShowSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<ShowSeat, String> {

    @Query("""
        SELECT s FROM ShowSeat s
        WHERE s.showId = :showId
        AND s.seatId IN :seatIds
    """)
    List<ShowSeat> findSeatsForBooking(@Param("seatIds") List<String> seatIds, @Param("showId") String showId);

    @Modifying
    @Query("""
        UPDATE ShowSeat s
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
