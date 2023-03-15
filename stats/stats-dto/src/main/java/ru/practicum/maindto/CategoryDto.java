package ru.practicum.maindto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CategoryDto {
    long id;
    @NotNull
    @NotBlank
    String name;
}