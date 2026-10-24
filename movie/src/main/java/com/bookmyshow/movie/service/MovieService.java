package com.bookmyshow.movie.service;

import com.bookmyshow.common.models.AllTheatorsMovie;
import com.bookmyshow.common.models.Genre;
import com.bookmyshow.common.models.Language;
import com.bookmyshow.common.models.Movie;
import com.bookmyshow.common.repository.GenreRepository;
import com.bookmyshow.common.repository.LanguageRepository;
import com.bookmyshow.common.repository.MovieRepository;
import com.bookmyshow.movie.CreateMovieRequest;
import com.bookmyshow.movie.exception.MovieNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class MovieService {

    private final MovieRepository movieRepository;
    private final LanguageRepository languageRepository;
    private final GenreRepository genreRepository;
    private final ModelMapper modelMapper;

    public Movie createMovie(CreateMovieRequest movieRequest, MultipartFile poster) throws IOException {

        log.info("Creating movie with title: {}", movieRequest.getTitle());

        Movie movie = modelMapper.map(movieRequest, Movie.class);

        if (movieRequest.getLanguageIds() != null && !movieRequest.getLanguageIds().isEmpty()) {
            List<Language> languages = languageRepository.findAllById(movieRequest.getLanguageIds());
            if (languages.size() != movieRequest.getLanguageIds().size()) {
                log.warn("Some language IDs were not found. Requested: {}, Found: {}",
                        movieRequest.getLanguageIds().size(), languages.size());
            }
            movie.setLanguages(new HashSet<>(languages));
        } else if (movie.getLanguages() == null) {
            movie.setLanguages(new HashSet<>());
        }

        if (movieRequest.getGenreIds() != null && !movieRequest.getGenreIds().isEmpty()) {
            List<Genre> genres = genreRepository.findAllById(movieRequest.getGenreIds());
            if (genres.size() != movieRequest.getGenreIds().size()) {
                log.warn("Some genre IDs were not found. Requested: {}, Found: {}",
                        movieRequest.getGenreIds().size(), genres.size());
            }
            movie.setGenres(new HashSet<>(genres));
        } else if (movie.getGenres() == null) {
            movie.setGenres(new HashSet<>());
        }

        if (poster != null && !poster.isEmpty()) {
            String fileName = UUID.randomUUID() + "_" + poster.getOriginalFilename();
            Path uploadDir = Paths.get("D:\\BookMyShow\\S3");
            Files.createDirectories(uploadDir);
            Files.copy(
                    poster.getInputStream(),
                    uploadDir.resolve(fileName),
                    StandardCopyOption.REPLACE_EXISTING
            );
            movie.setImage("/images/" + fileName);
        }

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

    @Transactional(readOnly = true)
    public Movie getMovieDetailsById(int movieId) {

        log.info("Fetching movie details with movieId: {}", movieId);

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

    public List<AllTheatorsMovie> getMovieByName(String movieName)
    {
       return movieRepository.findMovieByName(movieName);
    }
}