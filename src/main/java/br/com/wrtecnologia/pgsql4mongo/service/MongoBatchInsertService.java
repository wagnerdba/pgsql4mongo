package br.com.wrtecnologia.pgsql4mongo.service;

import br.com.wrtecnologia.pgsql4mongo.domain.mongodb.SensorDataDocument;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MongoBatchInsertService {

    public MongoBatchInsertService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    private final MongoTemplate mongoTemplate;

    public void batchInsert(List<SensorDataDocument> documents) {
        mongoTemplate.insert(documents, SensorDataDocument.class);
    }
}
