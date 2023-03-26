package ru.practicum.mainservice.request.service;

import ru.practicum.mainservice.request.model.dto.EventRequestStatusUpdate;
import ru.practicum.mainservice.request.model.dto.EventRequestStatusUpdateResult;
import ru.practicum.mainservice.request.model.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {

    EventRequestStatusUpdateResult changeStatusRequest(Long userId, Long eventId, EventRequestStatusUpdate statusUpdate);

    List<ParticipationRequestDto> getInfoUserRequestOther(Long userId);

    ParticipationRequestDto addRequestFromUser(Long userId, Long eventId);

    ParticipationRequestDto cancelRequestOwner(Long userId, Long requestId);

    List<ParticipationRequestDto> getRequestsInfoUser(Long userId, Long eventId);
}
