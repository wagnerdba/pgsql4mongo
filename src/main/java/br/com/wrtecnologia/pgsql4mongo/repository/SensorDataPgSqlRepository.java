package br.com.wrtecnologia.pgsql4mongo.repository;

import br.com.wrtecnologia.pgsql4mongo.domain.pgsql.SensorData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorDataPgSqlRepository extends JpaRepository<SensorData, Long> {
    Page<SensorData> findAll(Pageable pageable);

}
