package com.bookmyshow.user.repository;

import com.bookmyshow.common.models.BookingSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingSeatRepository extends JpaRepository<BookingSeat, String> {
}
