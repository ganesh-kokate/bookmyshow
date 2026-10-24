package com.bookmyshow.movie.dto.response;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShowDTO {

    private String showId;
    private String screenName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}

