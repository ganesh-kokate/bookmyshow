package com.bookmyshow.movie;

import com.bookmyshow.common.enums.MovieStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateMovieRequest {
    private String title;
    private int duration;
    private MovieStatus status;
    private List<Long> languageIds;
    private List<Long> genreIds;
}
