package ru.practicum.mainservice.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.request.model.dto.EventRequestStatusUpdate;
import ru.practicum.mainservice.request.service.RequestService;

import javax.validation.constraints.Min;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@Validated
public class PrivateRequestController {

    private final RequestService requestService;

    @GetMapping("users/{userId}/events/{eventId}/requests")
    public ResponseEntity<Object> getRequestsInfoUser(@PathVariable @Min(1) Long userId, @PathVariable @Min(1) Long eventId) {
        return new ResponseEntity<>(requestService.getRequestsInfoUser(userId, eventId), HttpStatus.OK);
    }

    @PatchMapping("users/{userId}/events/{eventId}/requests")
    public ResponseEntity<Object> changeStatusRequest(@PathVariable @Min(1) Long userId, @PathVariable @Min(1) Long eventId,
                                                      @RequestBody EventRequestStatusUpdate statusUpdate) {
        return new ResponseEntity<>(requestService.changeStatusRequest(userId, eventId, statusUpdate), HttpStatus.OK);
    }

    @GetMapping("users/{userId}/requests")
    public ResponseEntity<Object> getInfoUserRequestOther(@PathVariable @Min(1) Long userId) {
        return new ResponseEntity<>(requestService.getInfoUserRequestOther(userId), HttpStatus.OK);
    }

    @PostMapping("users/{userId}/requests")
    public ResponseEntity<Object> addRequestFromUser(@PathVariable Long userId, @RequestParam @Min(1) Long eventId) {
        return new ResponseEntity<>(requestService.addRequestFromUser(userId, eventId), HttpStatus.CREATED);
    }

    @PatchMapping("users/{userId}/requests/{requestId}/cancel")
    public ResponseEntity<Object> cancelRequestOwner(@PathVariable @Min(1) Long userId, @PathVariable @Min(1) Long requestId) {
        return new ResponseEntity<>(requestService.cancelRequestOwner(userId, requestId), HttpStatus.OK);
    }
}
