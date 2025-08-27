package com.example.statisticsservice.service;

import com.example.statisticsservice.domain.Account;
import com.example.statisticsservice.domain.Item;
import com.example.statisticsservice.domain.timeseries.DataPointId;
import com.example.statisticsservice.domain.timeseries.ItemMetric;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class StatisticsService {

    public void save(String accountName, @Valid Account account) {
        Instant now = LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        DataPointId pointId = new DataPointId("qwe", Date.from(now));

        List<ItemMetric> itemMetrics = account.getIncomes().stream().map()

    }

    private ItemMetric createItemMetric(Item item){

        return new ItemMetric(item.getTitle() , );


    }
}
