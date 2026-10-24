package com.bookmyshow.common.models;

import java.time.LocalDateTime;

public record AllTheatorsMovie(
        Integer movieId,
        String movieTitle,
        String showId,
        String theaterName,
        String screenName,
        LocalDateTime startTime,
        LocalDateTime endTime
) {}
