package ru.practicum.mainservice.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.comment.model.dto.CommentDto;
import ru.practicum.mainservice.comment.model.dto.NewCommentDto;
import ru.practicum.mainservice.comment.service.CommentService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/")
public class PrivateCommentController {
    private final CommentService commentService;

    @PostMapping(path = "events/{eventId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(@PathVariable long eventId,
                                    @PathVariable long userId,
                                    @RequestBody @Valid NewCommentDto newCommentDto) {
        return commentService.addComment(newCommentDto, eventId, userId);
    }

    @DeleteMapping("events/{eventId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable long userId, @PathVariable long commentId, @PathVariable long eventId) {
        commentService.deleteComment(commentId, userId);
    }

    @PatchMapping(path = "comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto updateComment(@RequestBody @Valid NewCommentDto commentDto,
                                    @PathVariable long userId,
                                    @PathVariable long commentId) {
        return commentService.updateComment(userId, commentId, commentDto);
    }
}
