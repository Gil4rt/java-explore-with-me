package ru.practicum.statserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.statserver.model.EndpointHit;
import ru.practicum.statserver.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<EndpointHit, Integer> {
    @Query(value = "SELECT new ru.practicum.statserver.model.ViewStats(h.app, h.uri, COUNT(h.ip)) " +
            "FROM EndpointHit h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "AND (:uris IS NULL OR h.uri IN :uris) " +
            "GROUP BY h.app, h.uri ORDER BY COUNT(h.ip) DESC")
    List<ViewStats> findNotUniqueIP(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("uris") List<String> uris);

    @Query(value = "SELECT new ru.practicum.statserver.model.ViewStats(h.app, h.uri, COUNT(DISTINCT h.ip)) " +
            "FROM EndpointHit h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "AND (:uris IS NULL OR h.uri IN :uris) " +
            "GROUP BY h.app, h.uri ORDER BY COUNT(h.ip) DESC")
    List<ViewStats> findUniqueIp(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("uris") List<String> uris);
}
