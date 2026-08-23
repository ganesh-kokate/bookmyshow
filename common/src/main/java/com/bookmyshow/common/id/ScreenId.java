package com.bookmyshow.common.id;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ScreenId implements Serializable {

    private String theater;
    private String screenName;
}
