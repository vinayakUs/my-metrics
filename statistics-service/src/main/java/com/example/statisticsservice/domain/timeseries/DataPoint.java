package com.example.statisticsservice.domain.timeseries;

import com.example.statisticsservice.domain.Currency;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Document(collation = "datapoints")
public class DataPoint {
    @Id
    public DataPointId id;
    private Set<ItemMetric> incomes;
    private Set<ItemMetric> expenses;
    private Map<StatisticMetric, BigDecimal> statistics;
    private Map<Currency, BigDecimal> rates;
}
