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
@RequestMapping("/admin/comments/{commentId}")
public class AdminCommentController {
    private final CommentService commentService;

    @DeleteMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable long commentId) {
        commentService.deleteCommentAdmin(commentId);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public CommentDto getComment(@PathVariable long commentId) {
        return commentService.getComment(commentId);
    }

    @PatchMapping()
    @ResponseStatus(HttpStatus.OK)
    public CommentDto updateComment(@RequestBody @Valid NewCommentDto commentDto,
                                    @PathVariable long commentId) {
        return commentService.updateCommentAdmin(commentId, commentDto);
    }
}
