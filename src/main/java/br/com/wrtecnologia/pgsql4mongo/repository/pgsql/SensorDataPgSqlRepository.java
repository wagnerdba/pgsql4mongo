package br.com.wrtecnologia.pgsql4mongo.repository.pgsql;

import br.com.wrtecnologia.pgsql4mongo.domain.pgsql.SensorData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorDataPgSqlRepository extends JpaRepository<SensorData, Long> {
    Page<SensorData> findAll(Pageable pageable);

}
