package ru.practicum.mainservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.statclient.StatClient;

@Configuration
public class ConfigStatClient {
    private final String serverUrl;

    public ConfigStatClient(@Value("${stats-service.url}") String serverUrl) {
        this.serverUrl = serverUrl;
    }

    @Bean
    public StatClient createStatistConfigClient() {
        return new StatClient(serverUrl);
    }
}
