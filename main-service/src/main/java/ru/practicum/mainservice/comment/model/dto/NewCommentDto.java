package ru.practicum.mainservice.comment.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewCommentDto {
    @NotBlank(message = "The text should not be blank")
    @Size(max = 2000, message = "Comment cannot be longer than 2000 characters.")
    String text;
}
