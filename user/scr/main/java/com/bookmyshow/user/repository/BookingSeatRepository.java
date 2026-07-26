package com.bookmyshow.user.repository;

import com.bookmyshow.user.models.BookingSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingSeatRepository extends JpaRepository<String, BookingSeat> {
}
