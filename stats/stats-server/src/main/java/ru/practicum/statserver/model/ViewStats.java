package ru.practicum.statserver.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ViewStats {

    private String app;
    private String uri;
    private long hits;
}
