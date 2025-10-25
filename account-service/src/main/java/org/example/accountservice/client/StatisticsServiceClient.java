package org.example.accountservice.client;

import lombok.extern.slf4j.Slf4j;
import org.example.accountservice.domain.Account;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class StatisticsServiceClient {
    private final WebClient webClient;

    public StatisticsServiceClient(@Qualifier("default-web-client") WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .clone()
                .baseUrl("http://STATISTICS-SERVICE")
                .build();

    }

    //    public Mono<String> postStatistic(Account account,String token){
//        log.info("calling postStatistic");
//        return webClient.post().uri("/").bodyValue(account)
//                .headers(headers -> headers.setBearerAuth(token))
//                .retrieve().bodyToMono(String.class);
//    }
//
    public Mono<String> postStatistic(Account account, String authorizedClientToken) {

        log.info("Calling Post Statistic");
        return
                webClient
                        .post()
                        .uri("/statistics/")
                        .headers(httpHeaders -> httpHeaders.setBearerAuth(authorizedClientToken))
                        .bodyValue(account)
                        .retrieve()
                        .onStatus(httpStatusCode -> httpStatusCode.isError(), (res) ->
                                Mono.error(new RuntimeException("Failed"))
                        )
                        .bodyToMono(String.class);

    }


}
