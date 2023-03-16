package ru.practicum.mainservice.category.service;

import ru.practicum.maindto.CategoryDto;
import ru.practicum.maindto.NewCategoryDto;

import java.util.Collection;

public interface CategoryService {

    CategoryDto create(NewCategoryDto newCategoryDto);

    void delete(long categoryId);

    CategoryDto update(CategoryDto categoryDto, long categoryId);

    Collection<CategoryDto> getAll(Integer from, Integer size);

    CategoryDto getCategoryDto(long categoryId);
}
