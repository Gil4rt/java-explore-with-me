package ru.practicum.mainservice.event.service;

import org.springframework.stereotype.Service;
import ru.practicum.mainservice.event.model.SortEv;
import ru.practicum.mainservice.event.model.dto.EventDto;
import ru.practicum.mainservice.event.model.dto.NewEventDto;
import ru.practicum.mainservice.event.model.dto.UpdateEventAdminRequest;
import ru.practicum.mainservice.event.model.dto.UpdateEventUserRequest;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
public interface EventService {
    Collection<EventDto> getEventsByAdmin(List<Long> users, List<String> states, List<Long> categories,
                                          LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);

    EventDto pathEventByAdmin(Long eventId, UpdateEventAdminRequest updateEventAdminRequest);

    EventDto getFullInfoEvent(Long id, HttpServletRequest request);

    Collection<EventDto> getAllEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                      Boolean onlyAvailable, SortEv sort, Integer from, Integer size, HttpServletRequest request);

    List<EventDto> getEventOwner(Long userId, int from, int size);

    EventDto addEventOwner(Long userId, NewEventDto newEventDto);

    EventDto getFullEventOwner(Long userId, Long eventId);

    EventDto pathEventOwner(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest);

}
