package br.com.wrtecnologia.pgsql4mongo.repository;

import br.com.wrtecnologia.pgsql4mongo.domain.mongodb.SensorDataDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SensorDataDocumentRepository extends MongoRepository<SensorDataDocument, String> {
    Optional<SensorDataDocument> findByIdPg(Long idPg);
}