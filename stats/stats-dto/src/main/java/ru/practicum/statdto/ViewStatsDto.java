package ru.practicum.statdto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ViewStatsDto {
    LocalDateTime start;
    LocalDateTime end;
    String[] uris;
    Boolean unique;
}
