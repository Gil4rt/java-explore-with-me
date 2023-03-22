package ru.practicum.statserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.statserver.model.EndpointHit;
import ru.practicum.statserver.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointHit, Integer> {
    @Query(value = "SELECT new ru.practicum.statserver.model.ViewStats(h.app, h.uri, COUNT (h.ip)) " +
            "FROM EndpointHit h " +
            "WHERE h.timestamp BETWEEN ?1 AND ?2 " +
            "AND (h.uri IN ?3 OR ?3 IS NULL) " +
            "GROUP BY h.app, h.uri ORDER BY COUNT(h.ip) DESC")
    List<ViewStats> findNotUniqueIP(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(value = "SELECT new ru.practicum.statserver.model.ViewStats(h.app, h.uri, COUNT (DISTINCT h.ip)) " +
            "FROM EndpointHit h " +
            "WHERE h.timestamp BETWEEN ?1 AND ?2 " +
            "AND (h.uri IN ?3 OR ?3 IS NULL) " +
            "GROUP BY h.app, h.uri ORDER BY COUNT(h.ip) DESC")
    List<ViewStats> findUniqueIp(LocalDateTime start, LocalDateTime end, List<String> uris);
}
