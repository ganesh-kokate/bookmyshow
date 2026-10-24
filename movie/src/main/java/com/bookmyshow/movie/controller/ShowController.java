package com.bookmyshow.movie.controller;


import com.bookmyshow.common.models.AddShowRequest;
import com.bookmyshow.common.models.AllotSeatRequest;
import com.bookmyshow.common.models.MovieShowsDTO;
import com.bookmyshow.common.models.Shows;
import com.bookmyshow.movie.dto.response.MovieShows;
import com.bookmyshow.movie.service.ShowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/show")
@RequiredArgsConstructor
@Log4j2
public class ShowController {

    private final ShowService showsService;

    @PostMapping("/add")
    public ResponseEntity<Shows> addShow(
            @RequestBody AddShowRequest request) {

        Shows show = showsService.creatShow(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(show);
    }

    @PostMapping("/allotseats")
    public String allotSeatsForShow(@RequestBody AllotSeatRequest request)
    {
        return showsService.allotSeatsForShow(request);
    }

    @DeleteMapping("/add")
    public String removeShow(@PathVariable String showId) {

        return showsService.removeShow(showId);

    }

    @GetMapping("/movies/{movieId}")
    public List<MovieShowsDTO> getShowsByMovieId(
            @PathVariable int movieId) {

        return showsService.getShowsforMovie(movieId);
    }

}
