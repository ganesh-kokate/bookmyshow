package com.bookmyshow.seat.model.request;

import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.List;

@Data
public class ConfirmRequest {
    @NotNull
    private List<String> seatIds;
}
