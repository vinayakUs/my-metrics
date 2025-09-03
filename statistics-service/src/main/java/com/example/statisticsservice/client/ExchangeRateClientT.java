//package com.example.statisticsservice.client;
//
//import com.example.statisticsservice.domain.Currency;
//import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.ParameterizedTypeReference;
//import org.springframework.http.HttpHeaders;
//import org.springframework.stereotype.Service;
//import org.springframework.util.Assert;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.core.publisher.Mono;
//
//import java.math.BigDecimal;
//import java.time.Duration;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.atomic.AtomicReference;
//
//@Service
//public class ExchangeRateClient {
//
    //    @Value("${app.properties.webclient.exchangeClient.api}")
    //    private String apiKey;
//
//
//    private final WebClient webClient;
//
//    public ExchangeRateClient(WebClient.Builder webClientBuilder , @Value("${app.properties.webclient.exchangeClient.hostname}") String url) {
//        this.webClient = webClientBuilder.baseUrl(url)
////                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
//                .build();
//
//    }
//
//    private final AtomicReference<Map<Currency,BigDecimal>> cachedRates = new AtomicReference<>(new HashMap<>());
//
//
//
//        String response = webClient.get()
//                .uri(uriBuilder -> uriBuilder.path("/latest").queryParam("api_key" , apiKey).build() )
//                .retrieve()
//                .onStatus(
//                        statusCode ->!statusCode.is2xxSuccessful(),
//                        clientResponse -> clientResponse.bodyToMono(Map.class).flatMap(errorBody ->Mono.error(new RuntimeException("API Error : " +  errorBody)))
//                )
//                .bodyToMono(String.class)
//                .block();
//
//}
//
//private Map<Currency, BigDecimal> getRatesFallback(Throwable throwable){
//    System.err.println("CircuitBreaker triggered, using fallback: " + throwable.getMessage());
//
//
//}
//
//
//
//    public Map<Currency, BigDecimal> getCurrentRates() {
//
//      String response  =  webClient.get().uri(uriBuilder -> uriBuilder.path("/latest").queryParam("access_key", "apiKey").build())
//              .retrieve()
//              .onStatus(
//                      status-> !status.is2xxSuccessful(),
//                      clientResponse -> clientResponse.bodyToMono(String.class).flatMap(errorBody -> Mono.error(new RuntimeException("API Error: " + errorBody)))
//                      )
//              .bodyToMono(String.class)
//
//                .timeout(Duration.ofSeconds(10))
//                .block();
//        System.out.println(response);
//      return null;
//
//
//    }
//
//
//
//}
