package ru.practicum.mainservice.user.model.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    @Positive
    Long id;
    @NotBlank(message = "Name cannot be blank.")
    @Size(max = 50, message = "Name cannot be longer than 50 characters.")
    String name;
    @Email(message = "Email has the wrong format.")
    @NotBlank(message = "Email cannot be blank.")
    String email;
}
