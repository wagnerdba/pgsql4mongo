package br.com.wrtecnologia.pgsql4mongo.service.migration;

import br.com.wrtecnologia.pgsql4mongo.domain.MigrationJob;
import br.com.wrtecnologia.pgsql4mongo.domain.mongodb.SensorDataDocument;
import br.com.wrtecnologia.pgsql4mongo.domain.pgsql.SensorData;
import br.com.wrtecnologia.pgsql4mongo.repository.pgsql.SensorDataPgSqlRepository;
import br.com.wrtecnologia.pgsql4mongo.service.MongoBatchInsertService;
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
import java.util.concurrent.atomic.AtomicLong;

/*
@Service
public class ParallelMigrationService {

    private final SensorDataPgSqlRepository postgresRepository;
    private final MongoBatchInsertService mongoBatchInsertService;
    private final ExecutorService executor;

    // Contador para acompanhar o progresso
    private final AtomicLong recordsProcessed = new AtomicLong(0);

    public ParallelMigrationService(SensorDataPgSqlRepository postgresRepository,
                                    MongoBatchInsertService mongoBatchInsertService) {
        this.postgresRepository = postgresRepository;
        this.mongoBatchInsertService = mongoBatchInsertService;
        this.executor = Executors.newFixedThreadPool(32); // threads para o processamento paralelo
    }

    public void migrateData() {

        resetCounters(); // Resetar o contador sempre que a migração for iniciada

        long startTime = System.currentTimeMillis();
        int pageSize = 100000; // Tamanho de cada lote
        long totalRecords = postgresRepository.count();
        long totalPages = (totalRecords + pageSize - 1) / pageSize; // Calcula o número de páginas

        // Criar uma lista de CompletableFutures para esperar todas as tarefas assíncronas
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (int page = 0; page < totalPages; page++) {
            final int currentPage = page;
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                try {

                    // Pageable pageable = PageRequest.of(currentPage, pageSize);

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

                    mongoBatchInsertService.batchInsert(documents);

                    // Atualiza o contador após inserir o lote
                    long processed = recordsProcessed.addAndGet(sensorDataList.size());

                    // Exibe o progresso a cada 1000 registros
                    /*
                    if (processed % 1000 == 0) {
                        System.out.println(processed + " registros inseridos.");
                    }
                    */
/*
                } catch (Exception e) {
                    System.err.println("Erro ao processar pagina " + currentPage + ": " + e.getMessage());
                }
            }, executor);

            futures.add(future);
        }

        // Espera todas as tarefas assíncronas terminarem
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        // Calcula o tempo total gasto
        long endTime = System.currentTimeMillis();
        long durationMillis = endTime - startTime;

        // Convertendo a duração para horas, minutos e segundos
        long hours = durationMillis / (1000 * 60 * 60);
        long minutes = (durationMillis / (1000 * 60)) % 60;
        long seconds = (durationMillis / 1000) % 60;

        // Exibe a mensagem final com o tempo e o total de registros processados
        System.out.println(String.format("Migracao concluida! Tempo gasto: %02d:%02d:%02d. Total de registros inseridos: %d",
                hours, minutes, seconds, recordsProcessed.get()));
    }

    public long getRecordsProcessed() {
        return recordsProcessed.get();
    }

    public void resetCounters() {
        recordsProcessed.set(0); // Reseta o contador de registros processados
    }
}
*/
@Service
public class ParallelMigrationService {

    private final SensorDataPgSqlRepository postgresRepository;
    private final MongoBatchInsertService mongoBatchInsertService;
    private final ExecutorService executor;

    // Mapeamento de jobId para o progresso do job
    private final ConcurrentHashMap<String, MigrationJob> activeJobs = new ConcurrentHashMap<>();

    public ParallelMigrationService(SensorDataPgSqlRepository postgresRepository,
                                    MongoBatchInsertService mongoBatchInsertService) {
        this.postgresRepository = postgresRepository;
        this.mongoBatchInsertService = mongoBatchInsertService;
        // this.executor = Executors.newFixedThreadPool(16);
        this.executor = Executors.newCachedThreadPool();
    }

    public String migrateData() {
        String jobId = generateJobId();
        long totalRecords = postgresRepository.count();
        MigrationJob job = new MigrationJob(jobId, totalRecords);
        activeJobs.put(jobId, job);

        CompletableFuture.runAsync(() -> executeMigration(job), executor);

        return jobId;
    }

    // Método para gerar jobId
    private String generateJobId() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
        LocalDateTime now = LocalDateTime.now();
        return "job-" + now.format(formatter);
    }

    /*
    private void executeMigration(MigrationJob job) {
        long startTime = System.currentTimeMillis();
        int pageSize = 100000;
        long totalPages = (job.getTotalRecords() + pageSize - 1) / pageSize;

        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (int page = 0; page < totalPages; page++) {
            final int currentPage = page;
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
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

                    mongoBatchInsertService.batchInsert(documents);

                    job.incrementProcessedRecords(sensorDataList.size());
                } catch (Exception e) {
                    System.err.println("Erro no job " + job.getJobId() + " página " + currentPage + ": " + e.getMessage());
                }
            }, executor);
            futures.add(future);
        }

        // Espera todas as tarefas assíncronas terminarem
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        // Calcula o tempo total gasto
        long endTime = System.currentTimeMillis();
        long durationMillis = endTime - startTime;

        // Convertendo a duração para horas, minutos e segundos
        long hours = durationMillis / (1000 * 60 * 60);
        long minutes = (durationMillis / (1000 * 60)) % 60;
        long seconds = (durationMillis / 1000) % 60;

        // Exibe a mensagem final com o tempo e o total de registros processados
        System.out.println(String.format("Migracao concluida! Tempo gasto: %02d:%02d:%02d. Total de registros inseridos: %d",
                hours, minutes, seconds, job.getProcessedRecords()));

        // Remove o job da lista de ativos
        activeJobs.remove(job.getJobId());
        System.out.println("Job " + job.getJobId() + " concluído!");
    }
     */
    private void executeMigration(MigrationJob job) {
        long startTime = System.currentTimeMillis();
        int pageSize = 100000;
        long totalPages = (job.getTotalRecords() + pageSize - 1) / pageSize;

        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (int page = 0; page < totalPages; page++) {
            final int currentPage = page;
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                String threadName = Thread.currentThread().getName(); // Obtém o nome da thread
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

                    mongoBatchInsertService.batchInsert(documents);

                    job.incrementProcessedRecords(sensorDataList.size());
                    System.out.println(job.getJobId() + " - Página " + currentPage + " concluída pela thread: " + threadName);
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

        System.out.println(String.format(job.getJobId() + " - Migracao concluida! Tempo gasto: %02d:%02d:%02d. Total de registros inseridos: %d",
                hours, minutes, seconds, job.getProcessedRecords()));
        activeJobs.remove(job.getJobId());
    }
}
