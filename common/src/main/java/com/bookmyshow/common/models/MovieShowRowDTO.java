package com.bookmyshow.common.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MovieShowRowDTO {

    private String theaterId;
    private String theaterName;
    private String theaterCity;
    private String showId;
    private String screenName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
