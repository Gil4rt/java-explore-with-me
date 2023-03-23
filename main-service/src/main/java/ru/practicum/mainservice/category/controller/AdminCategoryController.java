package ru.practicum.mainservice.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.category.model.dto.CategoryDto;
import ru.practicum.mainservice.category.model.dto.NewCategoryDto;
import ru.practicum.mainservice.category.service.CategoryService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/categories")
@Validated
public class AdminCategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto create(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        return categoryService.create(newCategoryDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CategoryDto> update(@Valid @RequestBody CategoryDto categoryDto, @PathVariable Long id) {
        return new ResponseEntity<>(categoryService.update(categoryDto, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        categoryService.delete(id);
    }
}
