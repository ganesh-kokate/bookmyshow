package com.bookmyshow.movie.controller;

import com.bookmyshow.common.models.AllTheatorsMovie;
import com.bookmyshow.common.models.Movie;
import com.bookmyshow.common.response.ApiResponse;
import com.bookmyshow.movie.CreateMovieRequest;
import com.bookmyshow.movie.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/movie")
@RequiredArgsConstructor
@Log4j2
public class MovieController {

    private final MovieService movieService;

    // Create
    @PostMapping(
            value ="/addMovie",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ApiResponse<Movie>> createMovie(
            @RequestPart("movie") CreateMovieRequest movieRequest,
            @RequestPart(value = "poster", required = false) MultipartFile poster
    ) throws java.io.IOException {

        log.info("Received request to create movie: {}", movieRequest.getTitle());
        Movie createdMovie = movieService.createMovie(movieRequest, poster);


        ApiResponse<Movie> response = ApiResponse.<Movie>builder()
                .success(true)
                .message("Movie created successfully")
                .data(createdMovie)
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // Get all
    @GetMapping("/getAllMovies")
    public ResponseEntity<ApiResponse<List<Movie>>> getAllMovies() {

        log.info("Received request to fetch all movies");

        List<Movie> movies = movieService.getAllMovies();

        ApiResponse<List<Movie>> response = ApiResponse.<List<Movie>>builder()
                .success(true)
                .message("Movies fetched successfully")
                .data(movies)
                .build();

        return ResponseEntity.ok(response);
    }

    // Get by ID
    @GetMapping("/{movieId}")
    public ResponseEntity<ApiResponse<Movie>> getMovieById(
            @PathVariable int movieId) {

        log.info("Received request to fetch movie with movieId: {}",
                movieId);

        Movie movie = movieService.getMovieById(movieId);

        ApiResponse<Movie> response = ApiResponse.<Movie>builder()
                .success(true)
                .message("Movie fetched successfully")
                .data(movie)
                .build();

        return ResponseEntity.ok(response);
    }

    // Update
    @PutMapping("/update/{movieId}")
    public ResponseEntity<ApiResponse<Movie>> updateMovie(
            @PathVariable int movieId,
            @RequestBody Movie movie) {

        log.info("Received request to update movie with movieId: {}",
                movieId);

        Movie updatedMovie = movieService.updateMovie(movieId, movie);

        ApiResponse<Movie> response = ApiResponse.<Movie>builder()
                .success(true)
                .message("Movie updated successfully")
                .data(updatedMovie)
                .build();

        return ResponseEntity.ok(response);
    }

    // Delete
    @DeleteMapping("/delete/{movieId}")
    public ResponseEntity<ApiResponse<Void>> deleteMovie(
            @PathVariable int movieId) {

        log.info("Received request to delete movie with movieId: {}",
                movieId);

        movieService.deleteMovie(movieId);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .message("Movie deleted successfully")
                .data(null)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/get/{movieName}")
    public List<AllTheatorsMovie> getMovieByName(
            @PathVariable("movieName") String movieName) {

       return movieService.getMovieByName(movieName);
    }


}
