package org.example.statisticsservice.repository;

import org.example.statisticsservice.domain.timepoint.DataPoint;
import org.springframework.data.repository.CrudRepository;

import javax.xml.crypto.Data;
import java.util.Optional;

public interface DataPointRepository extends CrudRepository<DataPoint,Long> {

    default Optional<DataPoint> saveDataPoint(DataPoint dataPoint) {
        return Optional.of(save(dataPoint));
    }

}
