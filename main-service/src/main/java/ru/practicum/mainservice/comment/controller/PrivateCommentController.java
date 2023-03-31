package ru.practicum.mainservice.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.comment.model.dto.CommentDto;
import ru.practicum.mainservice.comment.model.dto.NewCommentDto;
import ru.practicum.mainservice.comment.service.CommentService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/users/{userId}/events/{eventId}/comments")
public class PrivateCommentController {
    private final CommentService commentService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(@PathVariable long eventId,
                                    @PathVariable long userId,
                                    @RequestBody @Valid NewCommentDto newCommentDto) {
        return commentService.addComment(newCommentDto, eventId, userId);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable long userId, @PathVariable long commentId) {
        commentService.deleteComment(commentId, userId);
    }
}
