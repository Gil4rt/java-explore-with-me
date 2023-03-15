package ru.practicum.statserver.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ViewStats {
    private String app;
    private String uri;
    private long hits;
}
