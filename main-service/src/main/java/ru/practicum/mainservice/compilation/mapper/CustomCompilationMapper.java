package ru.practicum.mainservice.compilation.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.mainservice.compilation.model.Compilation;
import ru.practicum.mainservice.compilation.model.dto.NewCompilationDto;
import ru.practicum.mainservice.event.model.Event;

import java.util.HashSet;
import java.util.Set;

@Component
public class CustomCompilationMapper {

    public Compilation mapCompilationDtoToCompilation(NewCompilationDto newCompilationDto, Set<Event> events) {
        Compilation compilation = new Compilation();
        compilation.setTitle(newCompilationDto.getTitle());
        compilation.setPinned(newCompilationDto.getPinned());
        compilation.setEvents(new HashSet<>());
        for (Long eventId : newCompilationDto.getEvents()) {
            for (Event event : events) {
                if (event.getId().equals(eventId)) {
                    compilation.getEvents().add(event);
                    break;
                }
            }
        }
        return compilation;
    }
}
