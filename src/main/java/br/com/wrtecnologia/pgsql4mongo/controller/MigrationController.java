package br.com.wrtecnologia.pgsql4mongo.controller;

import br.com.wrtecnologia.pgsql4mongo.domain.mongodb.SensorDataDocument;
import br.com.wrtecnologia.pgsql4mongo.service.MongoBatchService;
import br.com.wrtecnologia.pgsql4mongo.service.migration.ParallelMigrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class MigrationController {

    private final ParallelMigrationService migrationService;

    public MigrationController(ParallelMigrationService migrationService, MongoBatchService documentoService) {
        this.migrationService = migrationService;
        this.documentoService = documentoService;
    }

    private final MongoBatchService documentoService;

    @GetMapping("/migrate")
    public String showMigrationPage() {
        return """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Iniciar Migração</title>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            background-color: #f0f4f8;
                            margin: 0;
                            padding: 0;
                            display: flex;
                            justify-content: center;
                            align-items: center;
                            height: 100vh;
                        }
                
                        .container {
                            text-align: center;
                            background-color: #ffffff;
                            padding: 40px;
                            border-radius: 10px;
                            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
                            max-width: 400px;
                            width: 100%;
                        }
                
                        h1 {
                            color: red;
                            font-size: 2.5em;
                            margin-bottom: 20px;
                        }
                
                        p {
                            font-size: 1.2em;
                            color: #333;
                            margin-bottom: 30px;
                        }
                
                        button {
                            background-color: #4CAF50;
                            color: white;
                            border: none;
                            padding: 15px 30px;
                            font-size: 1.2em;
                            border-radius: 5px;
                            cursor: pointer;
                            transition: background-color 0.3s ease;
                        }
                
                        button:hover {
                            background-color: #45a049;
                        }
                
                        button:active {
                            background-color: #388e3c;
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <h1>Iniciar Migração</h1>
                        <p>Clique no botão abaixo para iniciar a migração!</p>
                        <form action="/migrate/start" method="post">
                            <button type="submit">Iniciar Migração</button>
                        </form>
                    </div>
                </body>
                </html>
                """;
    }

    @PostMapping("/migrate/start")
    public String startMigration() {
        System.out.println("-".repeat(77));
        System.out.println("Migração iniciada! Aguarde...");
        System.out.println("-".repeat(77));
        migrationService.migrateData();
        return "Migracao Concluida! Consulte os logs da aplicacao para detalhes";
    }

    @CrossOrigin(origins = "*") // Permite todas as origens
    @GetMapping("/api/{idPg}")
    public ResponseEntity<SensorDataDocument> buscarPorIdPg(@PathVariable Long idPg) {
        System.out.println("ID recebido na URL: " + idPg);
        Optional<SensorDataDocument> documento = documentoService.buscarPorIdPg(idPg);
        return documento.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}