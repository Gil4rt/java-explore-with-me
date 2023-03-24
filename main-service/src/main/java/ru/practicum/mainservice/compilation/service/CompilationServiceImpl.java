package ru.practicum.mainservice.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.compilation.mapper.CompilationMapper;
import ru.practicum.mainservice.compilation.mapper.CustomCompilationMapper;
import ru.practicum.mainservice.compilation.model.Compilation;
import ru.practicum.mainservice.compilation.model.dto.CompilationDto;
import ru.practicum.mainservice.compilation.model.dto.NewCompilationDto;
import ru.practicum.mainservice.compilation.model.dto.UpdateCompilationDto;
import ru.practicum.mainservice.compilation.repository.CompilationRepository;
import ru.practicum.mainservice.event.mapper.EventMapper;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.event.model.dto.EventDto;
import ru.practicum.mainservice.event.repository.EventRepository;
import ru.practicum.mainservice.exception.NotFoundException;

import java.util.HashSet;
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
    private final CustomCompilationMapper customCompilationMapper;
    private final EventMapper eventMapper;

    @Override
    @Transactional
    public CompilationDto save(NewCompilationDto compilationDto) {
        if (compilationDto.getEvents() == null || compilationDto.getEvents().isEmpty()) {
            Compilation compilation = customCompilationMapper.mapCompilationDtoToCompilation(compilationDto, new HashSet<>());
            return compilationMapper.toCompilationDto(compilationRepository.save(compilation));
        }
        Set<Event> events = eventRepository.findAllByIdIn(compilationDto.getEvents());
        if (compilationDto.getEvents().size() != events.size()) {
            throw new NotFoundException("No events found");
        }
        Compilation compilation = customCompilationMapper.mapCompilationDtoToCompilation(compilationDto, events);
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
    public CompilationDto pathCompilation(Long compId, UpdateCompilationDto updateComp) {
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
        Pageable pageable;
        if (pinned == null) {
            pageable = PageRequest.of(from, size);
        } else {
            pageable = PageRequest.of(from, size, Sort.Direction.DESC, "id");
        }
        return compilationRepository.findAllByPinned(pinned, pageable).stream()
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
