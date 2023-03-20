package ru.practicum.mainservice.event.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.mainservice.category.model.Category;
import ru.practicum.mainservice.request.model.Request;
import ru.practicum.mainservice.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "events")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false, length = 500)
    String annotation;  // краткое описание
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    Category category;
    @OneToMany(mappedBy = "event", fetch = FetchType.EAGER)
    @JsonManagedReference
    List<Request> requests;
    @Column(name = "created_on", nullable = false)
    LocalDateTime createdOn;  // дата создания события
    @Column(name = "description", length = 1000)
    String description;  // полное описание
    @Column(name = "event_date")
    LocalDateTime eventDate;  // дата намеченного события
    @ManyToOne
    @JoinColumn(name = "initiator_id")
    User initiator;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "lat", column = @Column(name = "lat")),
            @AttributeOverride(name = "lon", column = @Column(name = "lon"))
    })
    Location location;
    @Column(nullable = false)
    Boolean paid;  // нужно ли оплачивать событие
    @Column(name = "participant_limit")
    Integer participantLimit;  // лимит количества участников

    @Column(name = "published_on")
    LocalDateTime publishedOn;  // дата и время публикации
    @Column(name = "request_moderation", nullable = false)
    Boolean requestModeration;  // нужна ли пре-модерация заявок на участие
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    State state;  // статус события
    @Column(nullable = false)
    String title; // заголовок
    @Column(nullable = false)
    Long views;  // количество просмотров

}
