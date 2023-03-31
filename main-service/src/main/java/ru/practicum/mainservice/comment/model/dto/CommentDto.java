package ru.practicum.mainservice.comment.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDto {
    @Positive
    long id;
    @NotBlank(message = "Text cannot be blank.")
    @Size(max = 2000, message = "Comment cannot be longer than 2000 characters.")
    String text;
    @NotBlank(message = "authorId cannot be blank.")
    long authorId;
    @NotBlank(message = "EventId cannot be blank.")
    long eventId;
}