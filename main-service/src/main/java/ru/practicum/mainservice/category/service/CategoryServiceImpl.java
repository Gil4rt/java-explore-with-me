package ru.practicum.mainservice.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.category.mapper.CategoryMapper;
import ru.practicum.mainservice.category.model.Category;
import ru.practicum.mainservice.category.model.dto.CategoryDto;
import ru.practicum.mainservice.category.model.dto.NewCategoryDto;
import ru.practicum.mainservice.category.repository.CategoryRepository;
import ru.practicum.mainservice.event.repository.EventRepository;
import ru.practicum.mainservice.exception.ConflictException;
import ru.practicum.mainservice.exception.NotFoundException;
import ru.practicum.mainservice.pagination.OffsetPageable;

import javax.validation.ValidationException;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final CategoryMapper mapper;
    @Override
    public CategoryDto create(NewCategoryDto newCategoryDto) {
        Category category;
        if (categoryRepository.findByName(newCategoryDto.getName()).isPresent()) {
            throw new ConflictException("This name is already taken");
        }
        try {
            category = categoryRepository.save(mapper.toCategory(newCategoryDto));
        } catch (DataIntegrityViolationException e) {
            throw new ValidationException("Error validation object");
        }
        return mapper.toCategoryDto(category);
    }

    @Override
    public void delete(long categoryId) {
        Category category = findById(categoryId);
        if (eventRepository.countByCategoryId(categoryId) > 0) {
            throw new ConflictException("Cannot delete a category with events");
        }
        categoryRepository.delete(category);
    }

    @Override
    public CategoryDto update(CategoryDto categoryDto, long categoryId) {
        Category category = findById(categoryId);
        String newName = categoryDto.getName();
        if (!category.getName().equals(newName) && categoryRepository.existsByName(newName)) {
            throw new ConflictException("Category with name " + newName + " already exists");
        }
        category.setName(categoryDto.getName());
        Category cat;
        try {
            cat = categoryRepository.save(category);
        } catch (DataIntegrityViolationException e) {
            throw new ValidationException("Error validation object");
        }
        return mapper.toCategoryDto(cat);
    }

    @Override
    public Collection<CategoryDto> getAll(Integer from, Integer size) {
        return categoryRepository.findAll(OffsetPageable.newInstance(from, size, Sort.by(
                Sort.Direction.ASC, "id"))).stream().map(mapper::toCategoryDto).collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryDto(long categoryId) {
        return mapper.toCategoryDto(findById(categoryId));
    }

    Category findById(long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() ->
                new NotFoundException("Category not found with id " + categoryId));
    }

}
