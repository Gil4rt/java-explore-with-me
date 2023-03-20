package ru.practicum.mainservice.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
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

import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final EventRepository eventRepository;
    private final CompilationRepository compilationRepository;

    @Override
    public CompilationDto save(NewCompilationDto compilationDto) {
        List<Event> events = eventRepository.findAllByIdIn(compilationDto.getEvents());
        if (compilationDto.getEvents().size() != events.size()) {
            throw new NotFoundException("No events found");
        }
        Compilation compilation = new Compilation(0L, compilationDto.getPinned(), compilationDto.getTitle(), events);
        try {
            return CompilationMapper.INSTANCE.toCompilationDto(compilationRepository.save(compilation));
        } catch (DataIntegrityViolationException e) {
            throw new ValidationException("Error validation object");
        }
    }

    @Override
    public void deleteCompById(Long compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() ->
                new NotFoundException(String.format("Set with id %d not found", compId)));
        compilationRepository.delete(compilation);
    }

    @Override
    public CompilationDto pathCompilation(Long compId, NewCompilationDto updateComp) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() ->
                new NotFoundException(String.format("Set with id %d not found", compId)));
        if (updateComp.getTitle() != null) {
            compilation.setTitle(updateComp.getTitle());
        }
        if (updateComp.getPinned() != null) {
            compilation.setPinned(updateComp.getPinned());
        }
        if (updateComp.getEvents() != null) {
            compilation.setEvents(eventRepository.findAllByIdIn(updateComp.getEvents()));
        }
        compilationRepository.save(compilation);
        return CompilationMapper.INSTANCE.toCompilationDto(compilation);
    }

    @Override
    public List<CompilationDto> getAllCompilations(Boolean pinned, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        return compilationRepository.findAll(pageable).stream()
                .filter(comp -> comp.getPinned() == pinned)
                .map(CompilationMapper.INSTANCE::toCompilationDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() ->
                new NotFoundException(String.format("Set with id %d not found", compId)));
        List<EventDto> events = compilation.getEvents()
                .stream()
                .map(EventMapper.INSTANCE::toEventDto)
                .collect(Collectors.toList());
        CompilationDto dto = CompilationMapper.INSTANCE.toCompilationDto(compilation);
        dto.setEvents(events);
        return dto;
    }
}