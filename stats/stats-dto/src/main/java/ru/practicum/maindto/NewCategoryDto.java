package ru.practicum.maindto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class NewCategoryDto {
    @NotNull
    @NotBlank
    private String name;
}
