package org.example.statisticsservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.statisticsservice.domain.Account;
import org.example.statisticsservice.domain.timepoint.DataPoint;
import org.example.statisticsservice.service.StatisticsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.time.ZoneId;
import java.util.Optional;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
@Slf4j
public class StatisticsController {
    private final StatisticsService statisticsService;

    @PostMapping("/")
    public ResponseEntity<DataPoint> saveAccountStatistics(
            @Valid @RequestBody Account account,
            @AuthenticationPrincipal Jwt jwt) {

        String username = jwt.getSubject();

        log.info("Saving statistics for username={}, subject={}", username, jwt.getSubject());

        Optional<DataPoint> savedOpt = statisticsService.save(account, username);

        DataPoint saved = statisticsService.save(account, username).orElseThrow(
                ()->new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Failed to save statistics"));



        String dateStr = saved.getId().getDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate().toString();

        URI location = URI.create(String.format("/statistics/%s/%s", username, dateStr));

        return ResponseEntity.created(location).body(saved);

    }

    @GetMapping("/test")
    public String test() {
        return "Ok";
    }

}
