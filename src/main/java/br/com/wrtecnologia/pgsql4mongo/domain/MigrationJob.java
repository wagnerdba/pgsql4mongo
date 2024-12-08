package br.com.wrtecnologia.pgsql4mongo.domain;

import java.util.concurrent.atomic.AtomicLong;

public class MigrationJob {
    private final String jobId;
    private final AtomicLong processedRecords;
    private final long totalRecords;

    public MigrationJob(String jobId, long totalRecords) {
        this.jobId = jobId;
        this.processedRecords = new AtomicLong(0);
        this.totalRecords = totalRecords;
    }

    public String getJobId() {
        return jobId;
    }

    public long getProcessedRecords() {
        return processedRecords.get();
    }

    public void incrementProcessedRecords(long count) {
        processedRecords.addAndGet(count);
    }

    public long getTotalRecords() {
        return totalRecords;
    }

    /*
    public double getProgressPercentage() {
        return totalRecords == 0 ? 0 : (processedRecords.get() * 100.0 / totalRecords);
    }
     */

    public int getProgressPercentage() {
        return totalRecords == 0 ? 0 : (int) Math.round(processedRecords.get() * 100.0 / totalRecords);
    }
}
