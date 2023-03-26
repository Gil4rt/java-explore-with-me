package ru.practicum.mainservice.event.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
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
    String annotation;  // short description
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    Category category;
    @OneToMany(mappedBy = "event")
    @JsonManagedReference
    @Fetch(FetchMode.SUBSELECT)
    List<Request> requests;
    @Column(name = "created_on", nullable = false)
    LocalDateTime createdOn;  // event creation date
    @Column(name = "description", length = 1000)
    String description;  //complete description
    @Column(name = "event_date")
    LocalDateTime eventDate;  //date of the scheduled event
    @ManyToOne
    @JoinColumn(name = "initiator_id")
    User initiator;
    @AttributeOverrides({
            @AttributeOverride(name = "lat", column = @Column(name = "lat")),
            @AttributeOverride(name = "lon", column = @Column(name = "lon"))
    })
    Location location;
    @Column(nullable = false)
    Boolean paid;  // whether the event must be paid
    @Column(name = "participant_limit")
    Integer participantLimit;  // number of participants limit
    @Column(name = "published_on")
    LocalDateTime publishedOn;  // date and time of publication
    @Column(name = "request_moderation", nullable = false)
    Boolean requestModeration;  // whether pre-moderation of requests is necessary
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    State state;   // event status
    @Column(nullable = false)

    String title; //title
}
