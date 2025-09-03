package com.example.statisticsservice.repository.converter;

import com.example.statisticsservice.domain.timeseries.DataPointId;
import com.mongodb.DBObject;
import org.springframework.core.convert.converter.Converter;

import java.util.Date;

public class DataPointIdReadConverter implements Converter<DBObject , DataPointId> {
    @Override
    public DataPointId convert(DBObject source) {

        Date date = (Date) source.get("date");
        String account = (String) source.get("account");

        return new DataPointId(account, date);
    }
}
