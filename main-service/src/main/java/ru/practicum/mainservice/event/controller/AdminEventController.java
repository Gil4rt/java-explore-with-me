package ru.practicum.mainservice.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.event.model.dto.EventDto;
import ru.practicum.mainservice.event.model.dto.UpdateEventAdminRequest;
import ru.practicum.mainservice.event.service.EventService;

import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Validated
public class AdminEventController {

    private final EventService eventService;

    @GetMapping("/events")
    public ResponseEntity<Collection<EventDto>> getEvents(@RequestParam(required = false) List<Long> users,
                                                          @RequestParam(required = false) List<String> states,
                                                          @RequestParam(required = false) List<Long> categories,
                                                          @RequestParam(required = false)
                                                          @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                          @RequestParam(required = false)
                                                          @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                          @RequestParam(defaultValue = "0") Integer from,
                                                          @RequestParam(defaultValue = "10") Integer size) {
        return new ResponseEntity<>(eventService.getEventsByAdmin(users, states, categories, rangeStart, rangeEnd,
                from, size), HttpStatus.OK);
    }

    @PatchMapping("/events/{eventId}")
    public ResponseEntity<EventDto> patchEvent(@PathVariable @Min(1) Long eventId,
                                               @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
        return new ResponseEntity<>(eventService.pathEventByAdmin(eventId, updateEventAdminRequest), HttpStatus.OK);
    }


}
