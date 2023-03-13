package ru.practicum.statserver.model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
public class ViewStats {

    private String app;
    private String uri;
    private long hits;
}
