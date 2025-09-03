package com.example.statisticsservice.service;

import com.example.statisticsservice.domain.Account;
import com.example.statisticsservice.domain.Item;
import com.example.statisticsservice.domain.Saving;
import com.example.statisticsservice.domain.timeseries.DataPoint;
import com.example.statisticsservice.domain.timeseries.DataPointId;
import com.example.statisticsservice.domain.timeseries.ItemMetric;
import com.example.statisticsservice.domain.timeseries.StatisticMetric;
import com.example.statisticsservice.repository.DataPointRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatisticsService {


    private final DataPointRepository dataPointRepository;

    public void save(String accountName, @Valid Account account) {
        Instant now = LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        DataPointId pointId = new DataPointId("qwe", Date.from(now));

        Set<ItemMetric> income = account.getIncomes().stream().map(this::createItemMetric).collect(Collectors.toSet());

        Set<ItemMetric> expense = account.getExpenses().stream().map(this::createItemMetric).collect(Collectors.toSet());

        Map<StatisticMetric, BigDecimal> statistics = createStatisticMetrics(income, expense, account.getSaving());

        DataPoint dataPoint = new DataPoint();
        dataPoint.setId(pointId);
        dataPoint.setIncomes(income);
        dataPoint.setExpenses(expense);
        dataPoint.setStatistics(statistics);

        log.info("created data point {}", dataPoint);
        dataPointRepository.save(dataPoint);

    }

    private Map<StatisticMetric, BigDecimal> createStatisticMetrics(Set<ItemMetric> income, Set<ItemMetric> expense, Saving saving) {
        BigDecimal savingsAmount = BigDecimal.ZERO; //@ get from saving object to usd TODO
        BigDecimal expenseAmount = expense.stream().map(ItemMetric::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal incomeAmount = income.stream().map(ItemMetric::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        return Map.of(StatisticMetric.INCOMES_AMOUNT , incomeAmount ,StatisticMetric.SAVING_AMOUNT , savingsAmount , StatisticMetric.EXPENSES_AMOUNT, expenseAmount);
    }

    private ItemMetric createItemMetric(Item item) {

        //@TODO need to add login for api exchange rate
        BigDecimal amount = new BigDecimal("23,2");

        return new ItemMetric(item.getTitle(), amount);


    }

    public List<DataPoint> findAccountById(String sub) {
        return dataPointRepository.findByIdAccount(sub);
    }
}
