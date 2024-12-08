package br.com.wrtecnologia.pgsql4mongo.service.migration;

import br.com.wrtecnologia.pgsql4mongo.domain.MigrationJob;
import br.com.wrtecnologia.pgsql4mongo.domain.mongodb.SensorDataDocument;
import br.com.wrtecnologia.pgsql4mongo.domain.pgsql.SensorData;
import br.com.wrtecnologia.pgsql4mongo.repository.pgsql.SensorDataPgSqlRepository;
import br.com.wrtecnologia.pgsql4mongo.service.MongoBatchService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class ParallelMigrationService {

    private final SensorDataPgSqlRepository postgresRepository;
    private final MongoBatchService mongoBatchService;
    private final ExecutorService executor;

    // Mapeamento de jobId para o progresso do job
    private final ConcurrentHashMap<String, MigrationJob> activeJobs = new ConcurrentHashMap<>();

    public ParallelMigrationService(SensorDataPgSqlRepository postgresRepository,
                                    MongoBatchService mongoBatchService) {
        this.postgresRepository = postgresRepository;
        this.mongoBatchService = mongoBatchService;
        // this.executor = Executors.newFixedThreadPool(16);
        this.executor = Executors.newCachedThreadPool();
    }

    public void migrateData() {
        String jobId = generateJobId();
        long totalRecords = postgresRepository.count();
        MigrationJob job = new MigrationJob(jobId, totalRecords);
        activeJobs.put(jobId, job);

        CompletableFuture.runAsync(() -> executeMigration(job), executor);

    }

    // Método para gerar jobId
    private String generateJobId() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
        LocalDateTime now = LocalDateTime.now();
        return "job-" + now.format(formatter);
    }

    private void executeMigration(MigrationJob job) {
        long startTime = System.currentTimeMillis();
        int pageSize = 100000;
        long totalPages = (job.getTotalRecords() + pageSize - 1) / pageSize;

        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (int page = 0; page < totalPages; page++) {
            final int currentPage = page;
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                String threadName = Thread.currentThread().getName();
                System.out.println(job.getJobId() + " - Página " + currentPage + " sendo processada pela thread: " + threadName);

                try {
                    Pageable pageable = PageRequest.of(currentPage, pageSize, Sort.by("id").ascending());
                    List<SensorData> sensorDataList = postgresRepository.findAll(pageable).getContent();

                    List<SensorDataDocument> documents = sensorDataList.stream()
                            .map(sensorData -> {
                                SensorDataDocument document = new SensorDataDocument();
                                document.setIdPg(sensorData.getId());
                                document.setTemperaturaCelsius(sensorData.getTemperaturaCelsius());
                                document.setTemperaturaFahrenheit(sensorData.getTemperaturaFahrenheit());
                                document.setUmidade(sensorData.getUmidade());
                                document.setDataHora(sensorData.getDataHora());
                                document.setUuid(sensorData.getUuid());
                                return document;
                            })
                            .toList();

                    mongoBatchService.batchInsert(documents);

                    job.incrementProcessedRecords(sensorDataList.size());
                    System.out.println(job.getJobId() + " - Página " + currentPage + " concluída pela thread: " + threadName+ " -> " + job.getProgressPercentage() + "%");
                } catch (Exception e) {
                    System.err.println("Erro no job " + job.getJobId() + " página " + currentPage + " pela thread: " + threadName + " - " + e.getMessage());
                }
            }, executor);
            futures.add(future);
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        long endTime = System.currentTimeMillis();
        long durationMillis = endTime - startTime;

        long hours = durationMillis / (1000 * 60 * 60);
        long minutes = (durationMillis / (1000 * 60)) % 60;
        long seconds = (durationMillis / 1000) % 60;

        System.out.println("-".repeat(77));
        System.out.println(String.format(job.getJobId() + " - Migracao concluida! Tempo gasto: %02d:%02d:%02d. Total de registros inseridos: %d",
                hours, minutes, seconds, job.getProcessedRecords()));
        System.out.println("-".repeat(77));
        activeJobs.remove(job.getJobId());
    }
}
