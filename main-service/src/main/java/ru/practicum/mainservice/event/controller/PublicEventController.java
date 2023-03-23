package ru.practicum.mainservice.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.event.model.SortEv;
import ru.practicum.mainservice.event.model.dto.EventDto;
import ru.practicum.mainservice.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@Validated
public class PublicEventController {
    private final EventService eventService;

    @GetMapping("events")
    public ResponseEntity<Collection<EventDto>> getAllEvents(@RequestParam(required = false) String text,
                                                             @RequestParam(required = false) List<Long> categories,
                                                             @RequestParam(required = false) Boolean paid,
                                                             @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                             LocalDateTime rangeStart,
                                                             @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                             LocalDateTime rangeEnd,
                                                             @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                                             @RequestParam(required = false) SortEv sort,
                                                             @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                             @Min(1) @RequestParam(defaultValue = "10") Integer size,
                                                             HttpServletRequest httpRequest) {
        return new ResponseEntity<>(eventService.getAllEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort,
                from, size, httpRequest), HttpStatus.OK);
    }

    @GetMapping("events/{id}")
    public ResponseEntity<EventDto> getFullInfoEvent(@Min(1) @PathVariable Long id, HttpServletRequest httpRequest) {
        return new ResponseEntity<>(eventService.getFullInfoEvent(id, httpRequest), HttpStatus.OK);
    }
}
