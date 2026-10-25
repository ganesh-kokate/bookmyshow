package com.bookmyshow.common.repository;

import com.bookmyshow.common.models.Seat;
import com.bookmyshow.common.models.ShowSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PhysicalSeatRepository extends JpaRepository<Seat, String> {

    @Query(value = """
        SELECT seat_id
        FROM seat
        WHERE theotorid = :theaterId
          AND screen_name = :screenName
        ORDER BY LEFT(seat_id, 1) ASC,
                 CAST(SUBSTRING(seat_id, 2) AS UNSIGNED) ASC
        LIMIT 1
        """, nativeQuery = true)
    String findDbSeatFrom(
            @Param("theaterId") String theaterId,
            @Param("screenName") String screenName);

    @Query(value = """
        SELECT seat_id
        FROM seat
        WHERE theotorid = :theaterId
          AND screen_name = :screenName
        ORDER BY LEFT(seat_id, 1) DESC,
                 CAST(SUBSTRING(seat_id, 2) AS UNSIGNED) DESC
        LIMIT 1
        """, nativeQuery = true)
    String findDbSeatTo(
            @Param("theaterId") String theaterId,
            @Param("screenName") String screenName);

}
