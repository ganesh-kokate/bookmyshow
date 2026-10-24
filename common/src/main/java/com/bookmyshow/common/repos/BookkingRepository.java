package com.bookmyshow.common.repos;

import com.bookmyshow.common.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookkingRepository extends JpaRepository<Booking, String> {
}
