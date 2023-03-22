package ru.practicum.mainservice.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.event.model.dto.EventDto;
import ru.practicum.mainservice.event.model.dto.NewEventDto;
import ru.practicum.mainservice.event.model.dto.UpdateEventUserRequest;
import ru.practicum.mainservice.event.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@Validated
public class PrivateEventController {
    private final EventService eventService;

    @GetMapping("users/{userId}/events")
    public ResponseEntity<List<EventDto>> getEventsOwner(@PathVariable @Min(1) Long userId,
                                                         @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                         @Min(1) @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(eventService.getEventOwner(userId, from, size), HttpStatus.OK);
    }

    @PostMapping("users/{userId}/events")
    public ResponseEntity<EventDto> addEventOwner(@PathVariable @Min(1) Long userId,
                                                  @Valid @RequestBody NewEventDto newEventDto) {
        return new ResponseEntity<>(eventService.addEventOwner(userId, newEventDto), HttpStatus.CREATED);
    }

    @GetMapping("users/{userId}/events/{eventId}")
    public ResponseEntity<EventDto> getFullEventOwner(@PathVariable @Min(1) Long userId, @PathVariable @Min(1) Long eventId) {
        return new ResponseEntity<>(eventService.getFullEventOwner(userId, eventId), HttpStatus.OK);
    }

    @PatchMapping("users/{userId}/events/{eventId}")
    public ResponseEntity<EventDto> pathEventOwner(@PathVariable @Min(1) Long userId, @PathVariable @Min(1) Long eventId,
                                                   @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        return new ResponseEntity<>(eventService.pathEventOwner(userId, eventId, updateEventUserRequest), HttpStatus.OK);
    }
}
