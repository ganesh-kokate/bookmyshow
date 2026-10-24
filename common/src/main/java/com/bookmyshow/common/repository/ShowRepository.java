package com.bookmyshow.common.repository;

import com.bookmyshow.common.models.MovieShowRowDTO;
import com.bookmyshow.common.models.Shows;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShowRepository extends JpaRepository<Shows,String> {

    @Query("""
    SELECT s
    FROM Shows s
    WHERE s.screen.theater.id = :theaterId
""")
    List<Shows> findByTheaterId(@Param("theaterId") String theaterId);


    @Query("""
    SELECT new com.bookmyshow.common.models.MovieShowRowDTO(
        s.screen.theater.id,
        s.screen.theater.name,
        s.screen.theater.city,
        s.showId,
        s.screen.screenName,
        s.startTime,
        s.endTime
    )
    FROM Shows s
    WHERE s.movie.movieId = :movieId
    ORDER BY s.screen.theater.name, s.startTime
""")
    List<MovieShowRowDTO> findMovieShows(
            @Param("movieId") Integer movieId
    );

}


