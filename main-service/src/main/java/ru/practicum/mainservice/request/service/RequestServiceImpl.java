package ru.practicum.mainservice.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.event.repository.EventRepository;
import ru.practicum.mainservice.exception.ConflictException;
import ru.practicum.mainservice.exception.NotFoundException;
import ru.practicum.mainservice.exception.ValidationException;
import ru.practicum.mainservice.request.mapper.RequestMapper;
import ru.practicum.mainservice.request.model.Request;
import ru.practicum.mainservice.request.model.RequestStatus;
import ru.practicum.mainservice.request.model.dto.EventRequestStatusUpdate;
import ru.practicum.mainservice.request.model.dto.EventRequestStatusUpdateResult;
import ru.practicum.mainservice.request.model.dto.ParticipationRequestDto;
import ru.practicum.mainservice.request.repository.RequestRepository;
import ru.practicum.mainservice.user.model.User;
import ru.practicum.mainservice.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.mainservice.event.model.State.PUBLISHED;
import static ru.practicum.mainservice.request.model.RequestStatus.*;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    private final RequestMapper mapper;

    @Override
    public List<ParticipationRequestDto> getRequestsInfoUser(Long userId, Long eventId) {
        if (userRepository.existsById(userId)) {
            Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found"));
            if (!event.getInitiator().getId().equals(userId)) {
                throw new ValidationException("This is not your event");
            }
            return event.getRequests().stream()
                    .map(mapper::toParticipationRequestDto)
                    .collect(Collectors.toList());

        }
        throw new NotFoundException("User with id" + userId + "not found");
    }

    @Transactional
    @Override
    public EventRequestStatusUpdateResult changeStatusRequest(Long userId, Long eventId, EventRequestStatusUpdate statusUpdate) {
        if (statusUpdate.getStatus() == null || statusUpdate.getRequestIds() == null) {
            throw new ConflictException("No status or identifiers to replace");
        }
        userRepository.findById(userId).orElseThrow(RuntimeException::new);
        Event event = eventRepository.findById(eventId).orElseThrow(RuntimeException::new);
        List<Long> requestsId = statusUpdate.getRequestIds();
        for (Long requestId : requestsId) {
            Request request = requestRepository.findById(requestId).orElseThrow(RuntimeException::new);
            if (request.getStatus() != RequestStatus.PENDING) {
                throw new ConflictException("The status of the application is unchangeable");
            }
            if (statusUpdate.getStatus() == RequestStatus.CONFIRMED) {
                if (event.getParticipantLimit() <= getConfirmedRequests(event.getRequests()).size()) {
                    throw new ConflictException("The limit of participants has expired");
                } else {
                    request.setStatus(RequestStatus.CONFIRMED);
                    mapper.toParticipationRequestDto(requestRepository.save(request));
                }
            } else if (statusUpdate.getStatus() == REJECTED) {
                request.setStatus(REJECTED);
                mapper.toParticipationRequestDto(requestRepository.save(request));
            }
            mapper.toParticipationRequestDto(requestRepository.save(request));
        }
        List<ParticipationRequestDto> confirmedRequests = requestRepository.findAllByIdInAndStatus(requestsId,
                CONFIRMED).stream().map(mapper::toRequestDto).collect(Collectors.toList());
        List<ParticipationRequestDto> rejectedRequests = requestRepository.findAllByIdInAndStatus(requestsId,
                REJECTED).stream().map(mapper::toRequestDto).collect(Collectors.toList());
        return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
    }

    public List<Request> getConfirmedRequests(List<Request> requests) {
        return requests.stream().filter(r -> r.getStatus() == CONFIRMED).collect(Collectors.toList());
    }

    @Override
    public List<ParticipationRequestDto> getInfoUserRequestOther(Long userId) {
        if (userRepository.existsById(userId)) {
            return requestRepository.findAllByRequesterId(userId)
                    .stream()
                    .map(mapper::toRequestDto)
                    .collect(Collectors.toList());
        }
        throw new NotFoundException("User with id" + userId + "not found");
    }

    @Transactional
    @Override
    public ParticipationRequestDto addRequestFromUser(Long userId, Long eventId) {
        userRepository.findById(userId).orElseThrow(RuntimeException::new);
        Event event = eventRepository.findById(eventId).orElseThrow(RuntimeException::new);
        Optional<Request> request = requestRepository.findByRequesterIdAndEventId(userId, eventId);
        if (request.isPresent()) {
            throw new ConflictException("Repeat request");
        }
        if (event.getState() != PUBLISHED) {
            throw new ConflictException("This event has not yet been published");
        }
        if (userId.equals(event.getInitiator().getId())) {
            throw new ConflictException("This is your event");
        }
        if (event.getParticipantLimit() <= getConfirmedRequests(event.getRequests()).size()) {
            throw new ConflictException("The limit of participants has expired");

        } else if (event.getRequestModeration() != null && !event.getRequestModeration()) {
            return mapper.toParticipationRequestDto(requestRepository.save(new Request(
                    0L, LocalDateTime.now(), event, userId, CONFIRMED)));
        } else {
            return mapper.toParticipationRequestDto(requestRepository.save(new Request(
                    0L, LocalDateTime.now(), event, userId, PENDING)));
        }
    }

    @Transactional
    @Override
    public ParticipationRequestDto cancelRequestOwner(Long userId, Long requestId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(
                "User with id" + userId + "not found"));
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Your query was not found"));
        if (!user.getId().equals(userId)) {
            throw new NotFoundException("You are not requesting your own request");
        }
        if (request.getStatus() == REJECTED || request.getStatus() == CANCELED) {
            throw new ConflictException("The application has already been cancelled");
        }
        request.setStatus(CANCELED);
        return mapper.toRequestDto(requestRepository.save(request));
    }
}
