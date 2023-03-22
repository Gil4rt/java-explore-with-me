package ru.practicum.statclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import ru.practicum.statdto.EndpointHitDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class StatClient {
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final WebClient webClient;

    @Value("${app.name}")
    private String appName;

    public StatClient(String url) {
        this.webClient = WebClient.create(url);
    }

    public void addHit(HttpServletRequest httpRequest) {
        EndpointHitDto endpointHitDto = EndpointHitDto.builder()
                .app(appName)
                .ip(httpRequest.getRemoteAddr())
                .uri(httpRequest.getRequestURI())
                .timestamp(LocalDateTime.now().format(DATE_TIME_FORMATTER))
                .build();
        log.info("IP: {}", endpointHitDto.getIp());
        log.info("URI: {}", endpointHitDto.getUri());
        webClient.post()
                .uri("/hit")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(endpointHitDto)
                .retrieve()
                .bodyToMono(EndpointHitDto.class)
                .block();
    }

}