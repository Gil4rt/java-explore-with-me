package ru.practicum.statdto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EndpointHitDto {
    Integer id;
    @NotBlank
    String app;
    @NotBlank
    String uri;
    @NotBlank
    String ip;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull
    Timestamp timestamp;
}
