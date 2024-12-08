package br.com.wrtecnologia.pgsql4mongo.service;

import br.com.wrtecnologia.pgsql4mongo.domain.mongodb.SensorDataDocument;
import br.com.wrtecnologia.pgsql4mongo.repository.mongodb.SensorDataDocumentRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MongoBatchService {

    private final MongoTemplate mongoTemplate;
    private final SensorDataDocumentRepository documentoRepository;

    public MongoBatchService(MongoTemplate mongoTemplate, SensorDataDocumentRepository documentoRepository) {
        this.mongoTemplate = mongoTemplate;
        this.documentoRepository = documentoRepository;
    }

    public void batchInsert(List<SensorDataDocument> documents) {
        mongoTemplate.insert(documents, SensorDataDocument.class);
    }

    public Optional<SensorDataDocument> buscarPorIdPg(Long idPg) {
        System.out.println("Buscando documento com idPg: " + idPg);
        return documentoRepository.findByIdPg(idPg);
    }
}
