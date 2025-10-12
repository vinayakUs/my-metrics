package org.example.accountservice.client;

import org.example.accountservice.domain.Account;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class StatisticsServiceClient {
    private final WebClient webClient;

    public StatisticsServiceClient(WebClient.Builder webClientBuilder) {
        this.webClient =webClientBuilder
                .clone()
                .baseUrl("http://statistics-service")
                .build();

    }

    public Mono<String> postStatistic(Account account){
        return webClient.post().uri("/").bodyValue(account).retrieve().bodyToMono(String.class);
    }



}
