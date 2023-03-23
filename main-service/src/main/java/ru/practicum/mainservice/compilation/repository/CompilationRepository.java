package ru.practicum.mainservice.compilation.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.mainservice.compilation.model.Compilation;


import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long>, QuerydslPredicateExecutor<Compilation> {

    List<Compilation> findAll(Specification<Compilation> spec, Pageable pageable);

}
