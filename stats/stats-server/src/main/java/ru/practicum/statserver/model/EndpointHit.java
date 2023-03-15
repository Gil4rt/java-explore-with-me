package ru.practicum.statserver.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@ToString
@Table(name = "stats")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EndpointHit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;
    @Column(name = "app", nullable = false, length = 100)
    String app;
    @Column(name = "uri", nullable = false, length = 150)
    String uri;
    @Column(name = "ip", nullable = false, length = 30)
    String ip;
    @Column(name = "timestamp")
    LocalDateTime timestamp;
}
