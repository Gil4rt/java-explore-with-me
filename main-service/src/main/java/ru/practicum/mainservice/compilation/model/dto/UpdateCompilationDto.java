package ru.practicum.mainservice.compilation.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateCompilationDto {
    @Size(max = 100, message = "Title cannot be longer than 100 characters.")
    String title;
    Boolean pinned;
    List<Long> events;
}
