package ru.practicum.mainservice.comment.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.mainservice.comment.model.Comment;
import ru.practicum.mainservice.comment.model.dto.CommentDto;
import ru.practicum.mainservice.comment.model.dto.NewCommentDto;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    Comment toComment(CommentDto commentDto);

    Comment toComment(NewCommentDto newCommentDto);

    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "event.id", target = "eventId")
    CommentDto toCommentDto(Comment comment);
}
