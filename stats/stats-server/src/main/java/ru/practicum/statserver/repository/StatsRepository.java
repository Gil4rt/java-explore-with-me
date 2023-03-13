package ru.practicum.statserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.statserver.model.EndpointHit;

@Repository
public interface StatsRepository extends JpaRepository<EndpointHit, Integer> {

}