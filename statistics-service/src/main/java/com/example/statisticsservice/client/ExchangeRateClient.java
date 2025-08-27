package com.example.statisticsservice.client;

import com.example.statisticsservice.domain.Currency;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
public class ExchangeRateClient {

    @Value("${app.properties.api.exchangeClient.hostname}")
    private String hostname;
    @Value("${app.properties.api.exchangeClient.api}")
    private String apiKey;

    private final WebClient webClient;

    public ExchangeRateClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(hostname)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .build();

    }

public BigDecimal convertCurrency(Currency from , Currency to, BigDecimal amount) {

        Map<Currency, BigDecimal> rates = getCurrentRates();
}
    public Map<Currency, BigDecimal> getCurrentRates() {

        webClient.get().uri(uriBuilder -> uriBuilder.path("/latest").queryParam("access_key", apiKey).build()).retrieve().bodyToMono(Map.class)
                .timeout(Duration.ofSeconds(10))
                .block();

    }



}
