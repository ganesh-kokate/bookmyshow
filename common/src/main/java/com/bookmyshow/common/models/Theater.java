package com.bookmyshow.common.models;

import com.bookmyshow.common.enums.TheaterStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "theator")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Theater {

    @Id
    @Column(name = "id", length = 50)
    private String id;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "city", length = 50)
    private String city;

    @Column(name = "state", length = 50)
    private String state;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @Builder.Default
    private TheaterStatus status = TheaterStatus.OPEN;
}
