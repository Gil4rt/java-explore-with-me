package ru.practicum.mainservice.compilation.service;


import ru.practicum.mainservice.compilation.model.dto.CompilationDto;
import ru.practicum.mainservice.compilation.model.dto.NewCompilationDto;

import java.util.List;

public interface CompilationService {
    CompilationDto save(NewCompilationDto compilationDto);

    void deleteCompById(Long compId);

    CompilationDto pathCompilation(Long compId, NewCompilationDto updateReq);

    List<CompilationDto> getAllCompilations(Boolean pinned, Integer from, Integer size);

    CompilationDto getCompilationById(Long compId);
}
