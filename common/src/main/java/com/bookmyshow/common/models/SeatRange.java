package com.bookmyshow.common.models;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SeatRange{
    String fromSeat;
    String toSeat;
    BigDecimal price;
}