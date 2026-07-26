package com.bookmyshow.seat.repository;

import com.bookmyshow.seat.model.seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatRepository extends JpaRepository<String, seat> {
}
