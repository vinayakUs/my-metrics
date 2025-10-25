package org.example.statisticsservice.service;

import lombok.RequiredArgsConstructor;
import org.example.statisticsservice.domain.Account;
import org.example.statisticsservice.domain.Currency;
import org.example.statisticsservice.domain.Item;
import org.example.statisticsservice.domain.Saving;
import org.example.statisticsservice.domain.timepoint.DataPoint;
import org.example.statisticsservice.domain.timepoint.DataPointId;
import org.example.statisticsservice.domain.timepoint.ItemMetric;
import org.example.statisticsservice.domain.timepoint.StatisticMetric;
import org.example.statisticsservice.repository.DataPointRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsService {
    private final RateService rateService;

    private final DataPointRepository dataPointRepository;

    public Optional<DataPoint> save(Account account , String accountName) {

        Instant now = LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        DataPointId dataPointId = new DataPointId(accountName , Date.from(now));

        Set<ItemMetric>  incomes = account.getIncomes().stream().map(this::createMetricItem).collect(Collectors.toSet());

        Set<ItemMetric> expenses = account.getExpenses().stream()
                .map(this::createMetricItem)
                .collect(Collectors.toSet());

        Map<StatisticMetric , BigDecimal> statistic = createStatisticMetrics(incomes , expenses , account.getSaving());
        System.out.println(statistic);

        DataPoint dataPoint = new DataPoint();
        dataPoint.setId(dataPointId);
        dataPoint.setIncomes(incomes);
        dataPoint.setExpenses(expenses);
        dataPoint.setStatistics(statistic);
        dataPoint.setRates(rateService.getCurrentRates());

        return dataPointRepository.saveDataPoint(dataPoint);

    }

    private Map<StatisticMetric, BigDecimal> createStatisticMetrics(Set<ItemMetric> incomes, Set<ItemMetric> expenses, Saving saving) {

        BigDecimal savingAmount = rateService.convert(saving.getCurrency() , Currency.getDefault(),saving.getAmount());

        BigDecimal expenseAmount =  expenses.stream().map(x -> x.getAmount()).reduce(BigDecimal.ZERO , (a,b)->a.add(b));

        BigDecimal incomeAmount = incomes.stream().map(x->x.getAmount()).reduce(BigDecimal.ZERO , (a,b)->a.add(b));


        return Map.of(
                StatisticMetric.INCOMES_AMOUNT , incomeAmount ,
                StatisticMetric.EXPENSES_AMOUNT , expenseAmount,
                StatisticMetric.SAVING_AMOUNT , savingAmount
        );



    }

    public ItemMetric createMetricItem(Item item){
        BigDecimal amount = rateService.convert(item.getCurrency() , Currency.getDefault() , item.getAmount());
        return new ItemMetric(item.getTitle() , amount);
    }


}
