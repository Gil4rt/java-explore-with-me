package ru.practicum.mainservice.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.maindto.CategoryDto;
import ru.practicum.maindto.NewCategoryDto;
import ru.practicum.mainservice.category.mapper.CategoryMapper;
import ru.practicum.mainservice.category.model.Category;
import ru.practicum.mainservice.category.repository.CategoryRepository;
import ru.practicum.mainservice.exception.NotFoundException;
import ru.practicum.mainservice.pagination.OffsetPageable;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;

    @Override
    public CategoryDto create(NewCategoryDto newCategoryDto) {
        return CategoryMapper.toCategoryDto(repository.save(CategoryMapper.toCategory(newCategoryDto)));
    }

    @Override
    public void delete(long categoryId) {
        repository.delete(findById(categoryId));
    }

    @Override
    public CategoryDto update(CategoryDto categoryDto, long categoryId) {
        Category category = findById(categoryId);
        category.setName(categoryDto.getName());
        return CategoryMapper.toCategoryDto(repository.save(category));
    }

    @Override
    public Collection<CategoryDto> getAll(Integer from, Integer size) {
        return repository.findAll(OffsetPageable.newInstance(from, size, Sort.by(Sort.Direction.ASC, "id")))
                .stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryDto(long categoryId) {
        return CategoryMapper.toCategoryDto(findById(categoryId));
    }

    Category findById(long categoryId) {
        return repository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category not found with id " + categoryId));
    }

}
