package ru.practicum.mainservice.comment.service;

import ru.practicum.mainservice.comment.model.dto.CommentDto;
import ru.practicum.mainservice.comment.model.dto.NewCommentDto;

import java.util.Collection;

public interface CommentService {
    CommentDto addComment(NewCommentDto newCommentDto, long eventId, long userId);

    CommentDto getComment(long commentId);

    Collection<CommentDto> getAllCommentsFromEvent(long eventId);

    void deleteComment(long commentId, long userId);

    CommentDto updateComment(long userId, long commentId, NewCommentDto commentDto);

    CommentDto updateCommentAdmin(long commentId, NewCommentDto commentDto);

    void deleteCommentAdmin(long comId);
}