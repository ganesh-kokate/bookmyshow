package com.bookmyshow.movie.dto.response;

import com.bookmyshow.common.models.Shows;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class MovieShows {
    String TheaterName;
    String TheaterCity;
    private List<ShowDTO> shows = new ArrayList<>();

}
