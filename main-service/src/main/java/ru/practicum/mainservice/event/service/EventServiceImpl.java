package ru.practicum.mainservice.event.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.category.model.Category;
import ru.practicum.mainservice.category.repository.CategoryRepository;
import ru.practicum.mainservice.event.mapper.EventMapper;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.event.model.QEvent;
import ru.practicum.mainservice.event.model.SortEv;
import ru.practicum.mainservice.event.model.State;
import ru.practicum.mainservice.event.model.dto.EventDto;
import ru.practicum.mainservice.event.model.dto.NewEventDto;
import ru.practicum.mainservice.event.model.dto.UpdateEventAdminRequest;
import ru.practicum.mainservice.event.model.dto.UpdateEventUserRequest;
import ru.practicum.mainservice.event.repository.EventRepository;
import ru.practicum.mainservice.exception.ConflictException;
import ru.practicum.mainservice.exception.NotFoundException;
import ru.practicum.mainservice.user.model.User;
import ru.practicum.mainservice.user.repository.UserRepository;
import ru.practicum.statclient.StatClient;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.mainservice.event.model.State.*;
import static ru.practicum.mainservice.event.model.StateAction.*;


@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final StatClient client;
    private final EventMapper mapper;

    @Transactional
    @Override
    public Collection<EventDto> getEventsByAdmin(List<Long> users, List<String> states, List<Long> categories, LocalDateTime rangeStart,
                                                 LocalDateTime rangeEnd, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        QEvent qEvent = QEvent.event;
        BooleanExpression expression = qEvent.id.isNotNull();
        if (users != null && users.size() > 0) {
            expression = expression.and(qEvent.initiator.id.in(users));
        }
        if (states != null && states.size() > 0) {
            expression = expression.and(qEvent.state.in(states.stream()
                    .map(State::valueOf)
                    .collect(Collectors.toUnmodifiableList())));
        }
        if (categories != null && categories.size() > 0) {
            expression = expression.and(qEvent.category.id.in(categories));
        }
        if (rangeStart != null) {
            expression = expression.and(qEvent.eventDate.goe(rangeStart));
        }
        if (rangeEnd != null) {
            expression = expression.and(qEvent.eventDate.loe(rangeEnd));
        }
        Collection<Event> events = eventRepository.findAll(expression, pageable).getContent();
        return events.stream().map(mapper::toEventDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public Collection<EventDto> getAllEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                             LocalDateTime rangeEnd, Boolean onlyAvailable, SortEv sort, Integer from, Integer size,
                                             HttpServletRequest httpRequest) {
        Pageable pageable = PageRequest.of(from, size);
        QEvent qEvent = QEvent.event;
        BooleanExpression expression = qEvent.state.eq(PUBLISHED);
        if (text != null) {
            expression = expression.and(qEvent.annotation.containsIgnoreCase(text).or(qEvent.description.containsIgnoreCase(text)));
        }
        if (paid != null) {
            expression = expression.and(qEvent.paid.eq(paid));
        }
        if (categories != null && categories.size() > 0) {
            expression = expression.and(qEvent.category.id.in(categories));
        }
        if (rangeStart != null) {
            expression = expression.and(qEvent.eventDate.goe(rangeStart));
        }
        if (rangeEnd != null) {
            expression = expression.and(qEvent.eventDate.loe(rangeEnd));
        }
        Collection<Event> events = eventRepository.findAll(expression, pageable).getContent();
        client.addHit(httpRequest);
        return events.stream().map(mapper::toEventDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventDto pathEventByAdmin(Long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found"));
        if (updateEventAdminRequest.getEventDate() != null
                && updateEventAdminRequest.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ConflictException("The time for editing the event has expired");
        }
        if (updateEventAdminRequest.getStateAction().equals(PUBLISH_EVENT)) {
            if (event.getState().equals(PUBLISHED) || event.getState().equals(CANCELED)) {
                throw new ConflictException("Unable to publish this event");
            }
            event.setState(PUBLISHED);
        }
        if (updateEventAdminRequest.getStateAction().equals(REJECT_EVENT)) {
            if (event.getState().equals(PUBLISHED)) {
                throw new ConflictException("Cannot cancel published event");
            }
            event.setState(CANCELED);
        }
        Optional.ofNullable(updateEventAdminRequest.getTitle()).ifPresent(event::setTitle);
        Optional.ofNullable(updateEventAdminRequest.getAnnotation()).ifPresent(event::setAnnotation);
        Optional.ofNullable(updateEventAdminRequest.getDescription()).ifPresent(event::setDescription);
        Optional.ofNullable(updateEventAdminRequest.getEventDate()).ifPresent(event::setEventDate);
        Optional.ofNullable(updateEventAdminRequest.getPaid()).ifPresent(event::setPaid);
        Optional.ofNullable(updateEventAdminRequest.getParticipantLimit()).ifPresent(event::setParticipantLimit);
        Optional.ofNullable(updateEventAdminRequest.getRequestModeration()).ifPresent(event::setRequestModeration);
        if (updateEventAdminRequest.getCategory() != null) {
            Category category = categoryRepository.findById(updateEventAdminRequest.getCategory())
                    .orElseThrow(() -> new NotFoundException("Category not found"));
            event.setCategory(category);
        }
        return mapper.toEventDto(eventRepository.save(event));
    }

    @Transactional
    @Override
    public EventDto getFullInfoEvent(Long id, HttpServletRequest httpRequest) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new NotFoundException("Event not found"));
        client.addHit(httpRequest);
        return mapper.toEventDto(event);
    }

    @Override
    public List<EventDto> getEventOwner(Long userId, int from, int size) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("userId: %d not found", userId));
        }
        Pageable pageable = PageRequest.of(from, size);
        List<Event> events = eventRepository.findAllByInitiatorId(userId, pageable);
        return events.stream()
                .map(mapper::toEventDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventDto addEventOwner(Long userId, NewEventDto newEventDto) {
        if (newEventDto == null) {
            throw new ValidationException("The header cannot be blank");
        }
        if (newEventDto.getEventDate() != null
                && newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ConflictException("the event time does not match the conditions");
        }
        if (newEventDto.getAnnotation() == null) {
            throw new ValidationException("The annotation cannot be empty");
        }
        User initiator = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("The user is not authorised"));
        Category category = categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new NotFoundException("Category not found"));
        Event event = new Event(0L, newEventDto.getAnnotation(), category, new ArrayList<>(), LocalDateTime.now(),
                newEventDto.getDescription(),
                newEventDto.getEventDate(), initiator, newEventDto.getLocation(),
                newEventDto.getPaid(), newEventDto.getParticipantLimit(),
                null, newEventDto.getRequestModeration(), PENDING, newEventDto.getTitle(), 0L);
        return mapper.toEventDto(eventRepository.save(event));
    }

    @Override
    public EventDto getFullEventOwner(Long userId, Long eventId) {
        if (userRepository.existsById(userId)) {
            Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException(
                    "Category with eventId:" + eventId + "not found"));
            if (userId.equals(event.getInitiator().getId())) {
                return mapper.toEventDto(event);
            }
        }
        throw new NotFoundException(String.format("userId: %d not found", userId));
    }

    @Transactional
    @Override
    public EventDto pathEventOwner(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest) {
        userRepository.findById(userId).orElseThrow(RuntimeException::new);
        Event event = eventRepository.findById(eventId).orElseThrow(RuntimeException::new);
        if (event.getState() == PUBLISHED) {
            throw new ConflictException("You cannot edit published events");
        }
        if (!userId.equals(event.getInitiator().getId())) {
            throw new NotFoundException("the event does not belong to the user");
        }
        if (updateEventUserRequest.getEventDate() != null
                && updateEventUserRequest.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ConflictException("The editing time for the event has expired");
        }
        if (updateEventUserRequest.getStateAction() == SEND_TO_REVIEW) {
            event.setState(State.PENDING);
        }
        if (updateEventUserRequest.getStateAction() == CANCEL_REVIEW) {
            event.setState(CANCELED);
        }
        if (updateEventUserRequest.getStateAction() == PUBLISH_EVENT) {
            event.setState(PUBLISHED);
        }
        Optional.ofNullable(updateEventUserRequest.getTitle()).ifPresent(event::setTitle);
        Optional.ofNullable(updateEventUserRequest.getAnnotation()).ifPresent(event::setAnnotation);
        Optional.ofNullable(updateEventUserRequest.getDescription()).ifPresent(event::setDescription);
        Optional.ofNullable(updateEventUserRequest.getEventDate()).ifPresent(event::setEventDate);
        Optional.ofNullable(updateEventUserRequest.getPaid()).ifPresent(event::setPaid);
        Optional.ofNullable(updateEventUserRequest.getParticipantLimit()).ifPresent(event::setParticipantLimit);
        Optional.ofNullable(updateEventUserRequest.getRequestModeration()).ifPresent(event::setRequestModeration);
        Event savedEvent = eventRepository.save(event);
        return mapper.toEventDto(savedEvent);
    }

}
