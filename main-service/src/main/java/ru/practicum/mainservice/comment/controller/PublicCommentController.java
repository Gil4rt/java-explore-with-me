package ru.practicum.mainservice.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.mainservice.comment.model.dto.CommentDto;
import ru.practicum.mainservice.comment.service.CommentService;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
public class PublicCommentController {

    private final CommentService commentService;

    @GetMapping(path = "/events/{eventId}/comments")
    @ResponseStatus(HttpStatus.OK)
    public Collection<CommentDto> getAllComments(@PathVariable long eventId) {
        return commentService.getAllCommentsFromEvent(eventId);
    }

    @GetMapping(path = "/comments/{commentId}")
    public CommentDto getCommentById(@PathVariable long commentId) {
        return commentService.getComment(commentId);
    }
}
