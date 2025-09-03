package com.example.statisticsservice.repository;

import com.example.statisticsservice.domain.timeseries.DataPoint;
import com.example.statisticsservice.domain.timeseries.DataPointId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataPointRepository extends CrudRepository<DataPoint, DataPointId> {
    List<DataPoint> findByIdAccount(String account);
}
