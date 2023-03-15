package ru.practicum.mainservice.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.mainservice.user.model.User;

import java.util.Collection;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Page<User> findAllByIdIn(Collection<Long> ids, Pageable pageable);
}