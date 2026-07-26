package com.bookmyshow.user.repository;

import com.bookmyshow.user.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<String, User> {
}
