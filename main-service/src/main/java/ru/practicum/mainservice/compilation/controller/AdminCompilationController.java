package ru.practicum.mainservice.compilation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.compilation.model.dto.CompilationDto;
import ru.practicum.mainservice.compilation.model.dto.NewCompilationDto;
import ru.practicum.mainservice.compilation.model.dto.UpdateCompilationDto;
import ru.practicum.mainservice.compilation.service.CompilationService;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping(path = "admin/compilations")
@RequiredArgsConstructor
@Validated
public class AdminCompilationController {

    private final CompilationService compilationService;

    @PostMapping
    public ResponseEntity<CompilationDto> addCompilation(@RequestBody @Valid NewCompilationDto compilationDto) {
        return new ResponseEntity<>(compilationService.save(compilationDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{compId}")
    public ResponseEntity<Void> deleteCompilation(@PathVariable @Min(1) Long compId) {
        compilationService.deleteCompById(compId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{compId}")
    public ResponseEntity<CompilationDto> patchCompilation(@PathVariable @Min(1) Long compId,
                                                           @Valid @RequestBody UpdateCompilationDto updateReq) {
        return new ResponseEntity<>(compilationService.pathCompilation(compId, updateReq), HttpStatus.OK);
    }
}
