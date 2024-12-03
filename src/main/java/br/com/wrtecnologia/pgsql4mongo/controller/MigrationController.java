package br.com.wrtecnologia.pgsql4mongo.controller;

import br.com.wrtecnologia.pgsql4mongo.domain.mongodb.SensorDataDocument;
import br.com.wrtecnologia.pgsql4mongo.service.SensorDataDocumentService;
import br.com.wrtecnologia.pgsql4mongo.service.migration.ParallelMigrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class MigrationController {

    private final ParallelMigrationService migrationService;

    public MigrationController(ParallelMigrationService migrationService) {
        this.migrationService = migrationService;
    }

    @Autowired
    private SensorDataDocumentService documentoService;

    /*
    @GetMapping("/migrate")
    public String migrate() {
        migrationService.migrateData();
        return "Migração iniciada! Para acompanhar acesse: http://localhost:8080/progress";
    }
     */

    /*
    @GetMapping("/progress")
    public String getProgress() {
        long recordsProcessed = migrationService.getRecordsProcessed();
        return "Registros processados: " + recordsProcessed;
    }
     */
    @GetMapping("/migrate")
    public String showMigrationPage() {
        return """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Iniciar Migração</title>
                </head>
                <body>
                    <h1>Iniciar Migração</h1>
                    <p>Clique no botão abaixo para iniciar a migração!</p>
                    <form action="/migrate/start" method="post">
                        <button type="submit">Iniciar Migração</button>
                    </form>
                </body>
                </html>
                """;
    }

    @PostMapping("/migrate/start")
    public String startMigration() {
        migrationService.migrateData();
        return "Migração Concluída! Consulte os logs da aplicação para detalhes";
    }

    @GetMapping("/progress")
    public ResponseEntity<Long> getProgress() {
        long recordsProcessed = migrationService.getRecordsProcessed();
        return ResponseEntity.ok(recordsProcessed);
    }

    @GetMapping("/{idPg}")
    public ResponseEntity<SensorDataDocument> buscarPorIdPg(@PathVariable Long idPg) {
        System.out.println("ID recebido na URL: " + idPg);
        Optional<SensorDataDocument> documento = documentoService.buscarPorIdPg(idPg);
        return documento.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

}