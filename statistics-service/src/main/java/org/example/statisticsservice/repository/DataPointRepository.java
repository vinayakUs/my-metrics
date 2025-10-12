package org.example.statisticsservice.repository;

import org.example.statisticsservice.domain.timepoint.DataPoint;
import org.springframework.data.repository.CrudRepository;

public interface DataPointRepository extends CrudRepository<DataPoint,Long> {
    
}
