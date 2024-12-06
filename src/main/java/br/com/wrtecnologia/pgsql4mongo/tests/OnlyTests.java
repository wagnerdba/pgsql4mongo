package br.com.wrtecnologia.pgsql4mongo.tests;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class OnlyTests {

    public static void main(String[] args) {
        // Executor personalizado com um pool de 4 threads
        ExecutorService executor = Executors.newFixedThreadPool(4);

        try {
            // Tarefa 1
            CompletableFuture.supplyAsync(() -> {
                //sleep(10); // Simula um processamento de 1 segundo
                return "Resultado da Tarefa 1";
            }, executor).thenAccept(result -> {
                System.out.println(result);
            });

            // Tarefa 2
            CompletableFuture.supplyAsync(() -> {
                //sleep(500); // Simula um processamento de 0,5 segundo
                return "Resultado da Tarefa 2";
            }, executor).thenAccept(result -> {
                System.out.println(result);
            });

            // Tarefa 3
            CompletableFuture.supplyAsync(() -> {
                //sleep(1500); // Simula um processamento de 1,5 segundo
                return "Resultado da Tarefa 3";
            }, executor).thenAccept(result -> {
                System.out.println(result);
            });

            // Tarefa 4
            CompletableFuture.supplyAsync(() -> {
                //sleep(200); // Simula um processamento de 0,2 segundo
                return "Resultado da Tarefa 4";
            }, executor).thenAccept(result -> {
                System.out.println(result);
            });

            // Aguarda algum tempo para que todas as tarefas sejam concluídas
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
            System.out.println("Execução finalizada!");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Execução foi interrompida: " + e.getMessage());
        }
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

