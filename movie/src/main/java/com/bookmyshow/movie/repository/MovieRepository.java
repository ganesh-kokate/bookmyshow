package com.bookmyshow.movie.repository;

import com.bookmyshow.common.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Integer> {
}