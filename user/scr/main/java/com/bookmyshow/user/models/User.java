package com.bookmyshow.user.models;

import com.bookmyshow.booking.models.Booking;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    String userId;
    String userName;
    @OneToMany(mappedBy = "user")
    private List<Booking> bookings = new ArrayList<>();
}
