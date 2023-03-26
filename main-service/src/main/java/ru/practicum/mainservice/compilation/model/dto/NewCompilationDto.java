package ru.practicum.mainservice.compilation.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewCompilationDto {
    @Size(max = 100, message = "Title cannot be longer than 100 characters.")
    @NotBlank(message = "Title cannot be blank")
    String title;
    @NotNull
    Boolean pinned;
    List<Long> events;

}