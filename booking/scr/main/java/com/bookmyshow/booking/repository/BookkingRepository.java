package com.bookmyshow.booking.repository;

import com.bookmyshow.booking.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookkingRepository extends JpaRepository<String, Booking> {
}
