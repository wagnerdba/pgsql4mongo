package br.com.wrtecnologia.pgsql4mongo.repository.mongodb;

import br.com.wrtecnologia.pgsql4mongo.domain.mongodb.SensorDataDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SensorDataDocumentRepository extends MongoRepository<SensorDataDocument, String> {
    Optional<SensorDataDocument> findByIdPg(Long idPg);
}