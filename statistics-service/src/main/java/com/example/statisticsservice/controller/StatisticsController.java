package com.example.statisticsservice.controller;

import com.example.statisticsservice.domain.Account;
import com.example.statisticsservice.service.StatisticsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @PutMapping("/{accountName}")
public void saveAccountStatistics(@PathVariable String accountName, @Valid @RequestBody Account account){
        statisticsService.save(accountName, account);

    }


}
