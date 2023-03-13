package ru.practicum.statserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.statdto.ViewStats;
import ru.practicum.statserver.model.EndpointHit;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Set;

@Repository
public interface StatsRepository extends JpaRepository<EndpointHit, Integer> {

    @Query("SELECT new ru.practicum.statdto.ViewStats(e.app, e.uri, " +
            "(CASE when :unique=true then COUNT(DISTINCT e.ip) else COUNT(e.ip) end)) " +
            "FROM EndpointHit AS e " +
            "WHERE e.timestamp BETWEEN :start AND :end AND e.uri IN (:uris) " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY COUNT(e.ip) DESC")
    Collection<ViewStats> findEndpointHitStatsByDatesAndUris(Timestamp start,
                                                             Timestamp end,
                                                             Set<String> uris,
                                                             boolean unique);


}
