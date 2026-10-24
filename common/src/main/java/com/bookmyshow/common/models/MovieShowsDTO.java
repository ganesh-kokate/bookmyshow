package com.bookmyshow.common.models;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MovieShowsDTO {

    private String theaterId;
    private String theaterName;
    private String theaterCity;

    private List<ShowDTO> shows = new ArrayList<>();
}