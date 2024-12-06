package br.com.wrtecnologia.pgsql4mongo.service;

import br.com.wrtecnologia.pgsql4mongo.domain.mongodb.SensorDataDocument;
import br.com.wrtecnologia.pgsql4mongo.repository.mongodb.SensorDataDocumentRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.*;

@Service
public class SensorDataDocumentService {

    public SensorDataDocumentService(SensorDataDocumentRepository documentoRepository) {
        this.documentoRepository = documentoRepository;
    }

    private final SensorDataDocumentRepository documentoRepository;

    public Optional<SensorDataDocument> buscarPorIdPg(Long idPg) {
        System.out.println("Buscando documento com idPg: " + idPg);
        return documentoRepository.findByIdPg(idPg);
    }

}
