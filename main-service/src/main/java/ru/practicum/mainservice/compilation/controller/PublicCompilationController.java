package ru.practicum.mainservice.compilation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.compilation.model.dto.CompilationDto;
import ru.practicum.mainservice.compilation.service.CompilationService;

import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class PublicCompilationController {

    private final CompilationService compilationService;

    @GetMapping("compilations")
    public ResponseEntity<List<CompilationDto>> getAllCompilations(@RequestParam(required = false) Boolean pinned,
                                                                   @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                                   @Min(1) @RequestParam(defaultValue = "10") Integer size) {
        return new ResponseEntity<>(compilationService.getAllCompilations(pinned, from, size), HttpStatus.OK);
    }

    @GetMapping("compilations/{compId}")
    public ResponseEntity<CompilationDto> getCompilationById(@PathVariable @Min(1) Long compId) {
        return new ResponseEntity<>(compilationService.getCompilationById(compId), HttpStatus.OK);
    }
}