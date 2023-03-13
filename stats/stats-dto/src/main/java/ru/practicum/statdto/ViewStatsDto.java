package ru.practicum.statdto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@NoArgsConstructor
@ToString
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ViewStatsDto {
    @NotBlank
    String app;
    @NotBlank
    String uri;
    long hits;
}
