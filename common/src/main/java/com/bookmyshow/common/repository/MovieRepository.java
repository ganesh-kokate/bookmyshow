package com.bookmyshow.common.repository;

import com.bookmyshow.common.models.AllTheatorsMovie;
import com.bookmyshow.common.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {
    @Query("""
    SELECT new com.bookmyshow.common.models.AllTheatorsMovie(
        m.movieId,
        m.title,
        s.showId,
        s.screen.theater.name,
        s.screen.screenName,
        s.startTime,
        s.endTime
    )
    FROM Movie m
    JOIN Shows s ON s.movie.movieId = m.movieId
    WHERE m.title = :movieName
""")
    List<AllTheatorsMovie> findMovieByName(@Param("movieName") String movieName);
}