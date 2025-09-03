package com.example.statisticsservice.client;

import com.example.statisticsservice.domain.Currency;
import com.example.statisticsservice.domain.ExchangeRatesResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ExchangeRateClient {

    @Value("${app.properties.webclient.exchangeClient.api}")
    private String apiKey;

    private final WebClient webClient;

    private final AtomicReference<Map<Currency, BigDecimal>> cachedRates =
            new AtomicReference<>(Collections.emptyMap());

    public ExchangeRateClient(WebClient.Builder webClientBuilder,
                              @Value("${app.properties.webclient.exchangeClient.hostname}") String url) {
        this.webClient = webClientBuilder.baseUrl(url).build();
    }

    @CircuitBreaker(name = "exchangeRateService", fallbackMethod = "getRatesFallback")
    public Map<Currency, BigDecimal> getExchangeRate(String key) {
        ExchangeRatesResponse response = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/latest")
                        .queryParam("access_key", key)
                        .build())
                .retrieve()
                .onStatus(
                        statusCode -> !statusCode.is2xxSuccessful(),
                        clientResponse -> clientResponse.bodyToMono(
                                        new ParameterizedTypeReference<Map<String, Object>>() {})
                                .flatMap(errorBody -> Mono.error(new RuntimeException("API Error: " + errorBody)))
                )
                .bodyToMono(ExchangeRatesResponse.class)
                .block();
//
//        if (response == null || response.getRates() == null || !response.isSuccess()) {
//            throw new RuntimeException("Invalid response from exchange API");
//        }

        Map<Currency, BigDecimal> currMap = new EnumMap<>(Currency.class);
        response.getRates().forEach((code, rate) -> {
            try {
                currMap.put(Currency.valueOf(code), new BigDecimal(rate));
            } catch (Exception ignored) {}
        });

        cachedRates.set(currMap);
        return currMap;
    }

    private Map<Currency, BigDecimal> getRatesFallback(Throwable ex) {
        System.err.println("Fallback triggered: " + ex.getMessage());
        return cachedRates.get();
    }
}
