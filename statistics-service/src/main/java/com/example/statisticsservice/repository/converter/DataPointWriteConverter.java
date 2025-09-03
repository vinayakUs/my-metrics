package com.example.statisticsservice.repository.converter;

import com.example.statisticsservice.domain.timeseries.DataPointId;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.core.convert.converter.Converter;
import org.bson.json.StrictJsonWriter;

import java.util.Date;

public class DataPointWriteConverter implements Converter<DataPointId, DBObject > {

    private static final int FIELDS = 2;



    @Override
    public DBObject convert(DataPointId id) {
        DBObject object = new BasicDBObject(FIELDS);
        object.put("date", id.getDate());
        object.put("account", id.getAccount());

        return object;
    }
}
