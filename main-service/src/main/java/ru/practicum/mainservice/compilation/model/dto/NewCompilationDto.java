package ru.practicum.mainservice.compilation.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewCompilationDto {
    @Size(max = 100, message = "Title cannot be longer than 1000 characters.")
    @NotBlank(message = "Description cannot be blank")
    private String title;
    @NotNull
    private Boolean pinned;
    @NotBlank
    private List<Long> events;

}