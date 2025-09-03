package com.example.statisticsservice.controller;

import com.example.statisticsservice.client.ExchangeRateClient;
import com.example.statisticsservice.domain.Account;
import com.example.statisticsservice.domain.Currency;
import com.example.statisticsservice.domain.timeseries.DataPoint;
import com.example.statisticsservice.service.StatisticsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;
    private final ExchangeRateClient exchangeRateClient;

    @GetMapping("/current")
    public List<DataPoint> getCurrentAccountStatistics(Authentication authentication) {
        log.info("Current account statistics {}", authentication.getName());
       return statisticsService.findAccountById(authentication.getName());
    }


    @PutMapping("/{accountName}")
    public void saveAccountStatistics(@PathVariable String accountName, @Valid @RequestBody Account account) {
        statisticsService.save(accountName, account);

    }

    @GetMapping("/currency")
    public Map<Currency, BigDecimal> getCurrencyStatistics(Authentication authentication,@RequestParam String key) {
        return  exchangeRateClient.getExchangeRate(key);

    }


}
