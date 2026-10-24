package com.bookmyshow.common.models;

import java.time.LocalDateTime;

public record AddShowRequest( Integer movieId,
                              LocalDateTime startTime) {
}
