package ru.practicum.mainservice.compilation.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.mainservice.event.model.dto.EventDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationDto {
    Long id;
    Boolean pinned;
    @Size(max = 100, message = "Title cannot be longer than 1000 characters.")
    @NotBlank(message = "Description cannot be blank")
    String title;
    List<EventDto> events;
}