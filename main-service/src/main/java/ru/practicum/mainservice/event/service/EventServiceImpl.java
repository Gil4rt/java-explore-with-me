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
import ru.practicum.mainservice.request.model.Request;
import ru.practicum.mainservice.user.model.User;
import ru.practicum.mainservice.user.repository.UserRepository;
import ru.practicum.statclient.StatClient;
import ru.practicum.statdto.ViewStats;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.practicum.mainservice.event.model.State.*;
import static ru.practicum.mainservice.event.model.StateAction.*;
import static ru.practicum.mainservice.request.model.RequestStatus.CONFIRMED;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final StatClient client;
    private final EventMapper mapper;

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
        return getEventDtos(events);
    }

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
        events.forEach(event -> {
            client.addHit(httpRequest, event.getId());
        });
        return getEventDtos(events);
    }

    @Transactional
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
        if (updateEventAdminRequest.getTitle() != null && !updateEventAdminRequest.getTitle().isBlank()) {
            event.setTitle(updateEventAdminRequest.getTitle());
        }
        if (updateEventAdminRequest.getAnnotation() != null && !updateEventAdminRequest.getAnnotation().isBlank()) {
            event.setAnnotation(updateEventAdminRequest.getAnnotation());
        }
        if (updateEventAdminRequest.getDescription() != null && !updateEventAdminRequest.getDescription().isBlank()) {
            event.setDescription(updateEventAdminRequest.getDescription());
        }
        if (updateEventAdminRequest.getEventDate() != null) {
            event.setEventDate(updateEventAdminRequest.getEventDate());
        }
        if (updateEventAdminRequest.getPaid() != null) {
            event.setPaid(updateEventAdminRequest.getPaid());
        }
        if (updateEventAdminRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());
        }
        if (updateEventAdminRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventAdminRequest.getRequestModeration());
        }
        if (updateEventAdminRequest.getCategory() != null) {
            Category category = categoryRepository.findById(updateEventAdminRequest.getCategory())
                    .orElseThrow(() -> new NotFoundException("Category not found"));
            event.setCategory(category);
        }
        return mapper.toEventDto(eventRepository.save(event));
    }

    @Override
    public EventDto getFullInfoEvent(Long id, HttpServletRequest httpRequest) {
        Event event = eventRepository.findByIdAndState(id, PUBLISHED).orElseThrow(() -> new NotFoundException("Event not found"));
        client.addHit(httpRequest);
        EventDto eventDto = mapper.toEventDto(event);
        eventDto.setViews(getViewStatsById(event.getId()));
        eventDto.setConfirmedRequests(getConfirmedRequestsCount(event.getRequests()));
        return eventDto;
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

    @Transactional
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
                newEventDto.getEventDate(), initiator, mapper.toLocation(newEventDto.getLocation()),
                newEventDto.getPaid(), newEventDto.getParticipantLimit(),
                null, newEventDto.isRequestModeration(), PENDING, newEventDto.getTitle());
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
        if (updateEventUserRequest.getTitle() != null && !updateEventUserRequest.getTitle().isBlank()) {
            event.setTitle(updateEventUserRequest.getTitle());
        }
        if (updateEventUserRequest.getAnnotation() != null && !updateEventUserRequest.getAnnotation().isBlank()) {
            event.setAnnotation(updateEventUserRequest.getAnnotation());
        }
        if (updateEventUserRequest.getDescription() != null && !updateEventUserRequest.getDescription().isBlank()) {
            event.setDescription(updateEventUserRequest.getDescription());
        }
        if (updateEventUserRequest.getEventDate() != null) {
            event.setEventDate(updateEventUserRequest.getEventDate());
        }
        if (updateEventUserRequest.getPaid() != null) {
            event.setPaid(updateEventUserRequest.getPaid());
        }
        if (updateEventUserRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventUserRequest.getParticipantLimit());
        }
        if (updateEventUserRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventUserRequest.getRequestModeration());
        }
        Event savedEvent = eventRepository.save(event);
        return mapper.toEventDto(savedEvent);
    }
    public int getConfirmedRequestsCount(List<Request> requests) {
        if(requests == null) {
            return 0;
        }
        return (int) requests.stream().filter(r -> r.getStatus() == CONFIRMED).count();
    }
    public Long getViewStatsById(Long eventId) {
        String uri = "/events/" + eventId.toString();
        List<String> uris = List.of(uri);
        List<ViewStats> viewStats = client.getListStats(LocalDateTime.now().minusYears(1L),
                LocalDateTime.now().plusYears(1L), uris, false);

        Map<Long, Long> map = viewStats.stream()
                .filter(statRecord -> statRecord.getApp().equals("ewm-service"))
                .collect(Collectors.toMap(
                                statRecord -> parseId(statRecord.getUri()),
                                ViewStats::getHits
                        )
                );
        return map.getOrDefault(eventId, 0L);
    }
    public Long parseId(String str) {
        int index = str.lastIndexOf('/');
        String strId = str.substring(index + 1);
        return Long.parseLong(strId);
    }
    private Collection<EventDto> getEventDtos(Collection<Event> events) {
        return events.stream()
                .map(event -> {
                    int confirmedRequestsCount = getConfirmedRequestsCount(event.getRequests());
                    EventDto eventDto = mapper.toEventDto(event);
                    eventDto.setConfirmedRequests(confirmedRequestsCount);
                    eventDto.setViews(getViewStatsById(event.getId()));
                    return eventDto;
                })
                .collect(Collectors.toList());
    }
}
