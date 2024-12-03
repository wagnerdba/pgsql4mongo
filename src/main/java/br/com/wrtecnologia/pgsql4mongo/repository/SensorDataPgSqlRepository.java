package br.com.wrtecnologia.pgsql4mongo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SensorDataPgSqlRepository<SensorData, Long> {
    Page<SensorData> findAll(Pageable pageable);
}
