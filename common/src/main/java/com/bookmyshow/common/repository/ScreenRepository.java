package com.bookmyshow.common.repository;

import com.bookmyshow.common.models.Screen;
import com.bookmyshow.common.models.ShowSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScreenRepository extends JpaRepository<Screen, String> {
    Optional<Screen> findByTheaterIdAndScreenName(
            String theaterId,
            String screenName
    );
}
