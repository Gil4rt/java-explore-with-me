package ru.practicum.mainservice.event.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.mainservice.event.model.StateAction;

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
public class UpdateEventAdminRequest {
    @Size(max = 1000, message = "Annotation cannot be longer than 1000 characters.")
    String annotation;
    Long category;
    @Size(max = 1000, message = "Description cannot be longer than 1000 characters.")
    String description;
    @JsonFormat(pattern = "yyy-MM-dd HH:mm:ss")
    @NotNull
    LocalDateTime eventDate;
    LocationDto location;
    @NotNull
    Boolean paid;
    @PositiveOrZero
    int participantLimit;
    @NotBlank
    Boolean requestModeration;
    @Size(max = 1000, message = "Title cannot be longer than 1000 characters.")
    String title;
    StateAction stateAction;

}
