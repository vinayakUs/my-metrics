package org.example.statisticsservice.service;

import org.example.statisticsservice.domain.Currency;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class RateService {

    public BigDecimal convert(Currency from, Currency to, BigDecimal amount) {


//        Map<Currency, BigDecimal> rates = getCurrentRates();
//        BigDecimal ratio = rates.get(to).divide(rates.get(from), 4, RoundingMode.HALF_UP);

        return amount.multiply(new BigDecimal(1));
    }

    public Map<Currency, BigDecimal> getCurrentRates(){
        return Map.of(Currency.USD, new BigDecimal(1),Currency.RUB, new BigDecimal(1) ,Currency.EUR, new BigDecimal(1)  );
    }

}
