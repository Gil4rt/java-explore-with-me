package ru.practicum.mainservice.comment.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDto {
    long id;
    String text;
    long authorId;
    long eventId;
    LocalDateTime createdOn;
    LocalDateTime editedOn;
}