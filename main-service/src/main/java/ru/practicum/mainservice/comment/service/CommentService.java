package ru.practicum.mainservice.comment.service;

import ru.practicum.mainservice.comment.model.dto.CommentDto;
import ru.practicum.mainservice.comment.model.dto.NewCommentDto;

public interface CommentService {
    CommentDto addComment(NewCommentDto newCommentDto, long eventId, long userId);

    CommentDto getComment(long commentId);

    void deleteComment(long commentId, long userId);

    CommentDto updateCommentAdmin(long commentId, NewCommentDto commentDto);

    void deleteCommentAdmin(long comId);
}