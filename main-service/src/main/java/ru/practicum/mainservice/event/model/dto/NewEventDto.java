package ru.practicum.mainservice.event.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewEventDto {
    @NotBlank
    @Size(max = 1000, message = "Annotation cannot be longer than 1000 characters.")
    String annotation;
    @NotNull
    Long category;
    @NotBlank
    @Size(max = 1000, message = "Description cannot be longer than 1000 characters.")
    String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull
    LocalDateTime eventDate;
    @Valid
    @NotNull
    LocationDto location;
    @NotNull
    Boolean paid;
    @PositiveOrZero
    int participantLimit;
    boolean requestModeration;
    @NotBlank
    @Size(max = 1000, message = "Title cannot be longer than 1000 characters.")
    String title;
}
