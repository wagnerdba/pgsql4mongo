package br.com.wrtecnologia.pgsql4mongo.controller;

import br.com.wrtecnologia.pgsql4mongo.service.migration.ParallelMigrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MigrationController {

    private final ParallelMigrationService migrationService;

    public MigrationController(ParallelMigrationService migrationService) {
        this.migrationService = migrationService;
    }

    @GetMapping("/migrate")
    public String migrate() {
        migrationService.migrateData();
        return "Migração iniciada! Para acompanhar acesse: http://localhost:8080/progress";
    }

    /*
    @GetMapping("/progress")
    public String getProgress() {
        long recordsProcessed = migrationService.getRecordsProcessed();
        return "Registros processados: " + recordsProcessed;
    }
     */

    @GetMapping("/progress")
    public ResponseEntity<Long> getProgress() {
        long recordsProcessed = migrationService.getRecordsProcessed();
        return ResponseEntity.ok(recordsProcessed);
    }

}