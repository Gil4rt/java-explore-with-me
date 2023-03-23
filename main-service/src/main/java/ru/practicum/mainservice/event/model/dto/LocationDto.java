package ru.practicum.mainservice.event.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Valid
@NotNull
public class LocationDto {
    Float lat;
    Float lon;
}
