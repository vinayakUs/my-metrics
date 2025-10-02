package org.example.statisticsservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.statisticsservice.domain.Account;
import org.example.statisticsservice.service.StatisticsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class StatisticsController {
    private final StatisticsService statisticsService;

    @PostMapping("/{username}")
    public void saveAccountStatistics(@Valid @RequestBody Account account, @PathVariable String username) {
        statisticsService.save(account,username);
    }

}
