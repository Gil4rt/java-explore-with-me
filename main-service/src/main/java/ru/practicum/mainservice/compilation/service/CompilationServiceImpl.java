package ru.practicum.mainservice.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.compilation.mapper.CompilationMapper;
import ru.practicum.mainservice.compilation.model.Compilation;
import ru.practicum.mainservice.compilation.model.dto.CompilationDto;
import ru.practicum.mainservice.compilation.model.dto.NewCompilationDto;
import ru.practicum.mainservice.compilation.repository.CompilationRepository;
import ru.practicum.mainservice.event.mapper.EventMapper;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.event.model.dto.EventDto;
import ru.practicum.mainservice.event.repository.EventRepository;
import ru.practicum.mainservice.exception.NotFoundException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {
    private final EventRepository eventRepository;
    private final CompilationRepository compilationRepository;

    private final CompilationMapper compilationMapper;
    private final EventMapper eventMapper;

    @Override
    @Transactional
    public CompilationDto save(NewCompilationDto compilationDto) {
        List<Long> eventIds = compilationDto.getEvents();
        if (eventIds == null || eventIds.isEmpty()) {
            throw new NotFoundException("No events provided");
        }
        Set<Event> events = eventRepository.findAllByIdIn(compilationDto.getEvents());
        if (compilationDto.getEvents().size() != events.size()) {
            throw new NotFoundException("No events found");
        }
        Compilation compilation = new Compilation(0L, compilationDto.getPinned(), compilationDto.getTitle(), events);
        return compilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    @Transactional
    public void deleteCompById(Long compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() ->
                new NotFoundException(String.format("Set with id %d not found", compId)));
        compilationRepository.delete(compilation);
    }

    @Override
    @Transactional
    public CompilationDto pathCompilation(Long compId, NewCompilationDto updateComp) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() ->
                new NotFoundException(String.format("Set with id %d not found", compId)));
        if (updateComp.getTitle() != null && !updateComp.getTitle().isBlank()) {
            compilation.setTitle(updateComp.getTitle());
        }
        if (updateComp.getPinned() != null) {
            compilation.setPinned(updateComp.getPinned());
        }
        if (updateComp.getEvents() != null) {
            compilation.setEvents(eventRepository.findAllByIdIn(updateComp.getEvents()));
        }
        compilationRepository.save(compilation);
        return compilationMapper.toCompilationDto(compilation);
    }

    @Override
    public List<CompilationDto> getAllCompilations(Boolean pinned, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        return compilationRepository.findAll(pageable).stream()
                .filter(comp -> comp.getPinned() == pinned)
                .map(compilationMapper::toCompilationDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() ->
                new NotFoundException(String.format("Set with id %d not found", compId)));
        List<EventDto> events = compilation.getEvents()
                .stream()
                .map(eventMapper::toEventDto)
                .collect(Collectors.toList());
        CompilationDto dto = compilationMapper.toCompilationDto(compilation);
        dto.setEvents(events);
        return dto;
    }
}
