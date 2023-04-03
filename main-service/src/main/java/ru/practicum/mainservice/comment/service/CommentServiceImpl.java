package ru.practicum.mainservice.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.comment.mapper.CommentMapper;
import ru.practicum.mainservice.comment.model.Comment;
import ru.practicum.mainservice.comment.model.dto.CommentDto;
import ru.practicum.mainservice.comment.model.dto.NewCommentDto;
import ru.practicum.mainservice.comment.repository.CommentRepository;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.event.repository.EventRepository;
import ru.practicum.mainservice.exception.ConflictException;
import ru.practicum.mainservice.exception.NotFoundException;
import ru.practicum.mainservice.user.model.User;
import ru.practicum.mainservice.user.repository.UserRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.mainservice.event.model.State.PENDING;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    @Override
    @Transactional
    public CommentDto addComment(NewCommentDto newCommentDto, long eventId, long userId) {
        Comment comment = Comment.builder()
                .text(newCommentDto.getText())
                .author(getUserById(userId))
                .event(getEventById(eventId))
                .createdOn(LocalDateTime.now())
                .build();
        return commentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public CommentDto getComment(long commentId) {
        return commentMapper.toCommentDto(getCommentById(commentId));
    }

    @Override
    public List<CommentDto> getAllCommentsFromEvent(long eventId) {
        return getEventById(eventId)
                .getComments()
                .stream()
                .map(commentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteComment(long commentId, long userId) {
        Comment comment = getCommentById(commentId);
        checkOwner(comment, userId);
        commentRepository.deleteById(commentId);
    }

    @Override
    @Transactional
    public CommentDto updateComment(long userId, long commentId, NewCommentDto commentDto) {
        Comment comment = getCommentById(commentId);
        checkOwner(comment, userId);
        checkCommentEditTime(comment);
        comment.setText(commentDto.getText());
        comment.setEditedOn(LocalDateTime.now());
        return commentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public CommentDto updateCommentAdmin(long commentId, NewCommentDto commentDto) {
        Comment comment = getCommentById(commentId);
        comment.setText(commentDto.getText());
        return commentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public void deleteCommentAdmin(long commentId) {
        commentRepository.deleteById(commentId);
    }

    private void checkOwner(Comment comment, long userId) {
        if (comment.getAuthor().getId() != userId) {
            throw new ConflictException(
                    String.format("User %d is not the owner of comment %d", userId, comment.getId()));
        }
    }

    private void checkCommentEditTime(Comment comment) {
        LocalDateTime createdOn = comment.getCreatedOn();
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(createdOn, now);
        if (duration.toHours() > 24) {
            throw new ConflictException("The comment cannot be edited as it is older than 24 hours");
        }
    }

    private Event getEventById(long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException(
                        String.format("Event %d not found", eventId))
        );
        if (event.getState() == PENDING) {
            throw new ConflictException("You cannot add comments to not published events");
        }
        return event;
    }

    private User getUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(
                        String.format("User %d not found", userId))
        );
    }

    private Comment getCommentById(long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException(
                        String.format("Comment %d not found", commentId))
        );
    }
}
