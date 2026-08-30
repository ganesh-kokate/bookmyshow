package com.bookmyshow.movie.service;

import com.bookmyshow.common.models.Movie;
import com.bookmyshow.movie.exception.MovieNotFoundException;
import com.bookmyshow.movie.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class MovieService {

    private final MovieRepository movieRepository;

    public Movie createMovie(Movie movie) {

        log.info("Creating movie with title: {}", movie.getTitle());

        Movie savedMovie = movieRepository.save(movie);

        log.info("Movie created successfully with movieId: {}",
                savedMovie.getMovieId());

        return savedMovie;
    }

    @Transactional(readOnly = true)
    public List<Movie> getAllMovies() {

        log.info("Fetching all movies");

        List<Movie> movies = movieRepository.findAll();

        log.info("Successfully fetched {} movies", movies.size());

        return movies;
    }

    @Transactional(readOnly = true)
    public Movie getMovieById(int movieId) {

        log.info("Fetching movie with movieId: {}", movieId);

        return movieRepository.findById(movieId)
                .orElseThrow(() -> {
                    log.warn("Movie not found with movieId: {}", movieId);

                    return new MovieNotFoundException(
                            "Movie not found with id: " + movieId
                    );
                });
    }

    public Movie updateMovie(int movieId, Movie movie) {

        log.info("Updating movie with movieId: {}", movieId);

        Movie existingMovie = movieRepository.findById(movieId)
                .orElseThrow(() -> {
                    log.warn("Movie not found for update with movieId: {}",
                            movieId);

                    return new MovieNotFoundException(
                            "Movie not found with id: " + movieId
                    );
                });

        existingMovie.setTitle(movie.getTitle());
        existingMovie.setDuration(movie.getDuration());
        existingMovie.setStatus(movie.getStatus());

        Movie updatedMovie = movieRepository.save(existingMovie);

        log.info("Movie updated successfully with movieId: {}",
                movieId);

        return updatedMovie;
    }

    public void deleteMovie(int movieId) {

        log.info("Deleting movie with movieId: {}", movieId);

        if (!movieRepository.existsById(movieId)) {
            log.warn("Movie not found for deletion with movieId: {}",
                    movieId);

            throw new MovieNotFoundException(
                    "Movie not found with id: " + movieId
            );
        }

        movieRepository.deleteById(movieId);

        log.info("Movie deleted successfully with movieId: {}",
                movieId);
    }
}