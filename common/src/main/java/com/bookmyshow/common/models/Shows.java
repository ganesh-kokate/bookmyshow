package com.bookmyshow.common.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "shows")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Shows {
    @Id
    @Column(name = "show_id", length = 50)
    private String showId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(
                    name = "theotorid",
                    referencedColumnName = "theotorid",
                    nullable = false
            ),
            @JoinColumn(
                    name = "screen_name",
                    referencedColumnName = "screen_name",
                    nullable = false
            )
    })
    private Screen screen;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;
}
