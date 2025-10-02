package org.example.statisticsservice.service;

import lombok.RequiredArgsConstructor;
import org.example.statisticsservice.domain.Account;
import org.example.statisticsservice.domain.Currency;
import org.example.statisticsservice.domain.Item;
import org.example.statisticsservice.domain.timepoint.DataPointId;
import org.example.statisticsservice.domain.timepoint.ItemMetric;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsService {
    private final RateService rateService;

    public void save(Account account , String accountName) {

        Instant now = LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        DataPointId dataPointId = new DataPointId(accountName , Date.from(now));

        Set<ItemMetric>  incomes = account.getIncomes().stream().map(this::createMetricItem).collect(Collectors.toSet());

        Set<ItemMetric> expenses = account.getExpenses().stream()
                .map(this::createMetricItem)
                .collect(Collectors.toSet());

//        Set<ItemMetric> income =account.getIncomes().stream().map(x->rateService.convert(x.getCurrency() , Currency.getDefault(),x.getAmount())).collect(Collectors.toSet());

    }

    public ItemMetric createMetricItem(Item item){
        BigDecimal amount = rateService.convert(item.getCurrency() , Currency.getDefault() , item.getAmount());
        return new ItemMetric(item.getTitle() , amount);
    }


}
