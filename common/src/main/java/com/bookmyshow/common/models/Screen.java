package com.bookmyshow.common.models;

import com.bookmyshow.common.enums.ScreenStatus;
import com.bookmyshow.common.id.ScreenId;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(ScreenId.class)
@Getter
@Setter
@Table(name = "screen")
public class Screen {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theotorid", referencedColumnName = "id")
    private Theater theater;

    @Id
    @Column(name = "screen_name", length = 50)
    private String screenName;

    @Column(name = "capacity")
    private int capacity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ScreenStatus status;
}
