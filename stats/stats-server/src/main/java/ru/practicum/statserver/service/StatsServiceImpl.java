package ru.practicum.statserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.statdto.EndpointHitDto;
import ru.practicum.statdto.ViewStats;
import ru.practicum.statserver.mapper.StatsMapper;
import ru.practicum.statserver.model.EndpointHit;
import ru.practicum.statserver.repository.StatsRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsRepository repository;
    private final StatsMapper mapper;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public EndpointHitDto create(EndpointHit hit) {
        return mapper.toDTO(repository.save(hit));
    }

    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, Set<String> uris, Boolean unique) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ViewStats> cq = cb.createQuery(ViewStats.class);
        Root<ViewStats> root = cq.from(ViewStats.class);

        cq.where(cb.between(root.get("timestamp"), start, end));

        if (uris != null && !uris.isEmpty()) {
            cq.where(root.get("uri").in(uris));
        }

        if (unique != null && unique) {
            cq.select(root.get("ip")).distinct(true);
        }

        TypedQuery<ViewStats> query = entityManager.createQuery(cq);
        return query.getResultList();
    }


}
