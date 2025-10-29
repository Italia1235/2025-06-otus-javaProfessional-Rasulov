package ru.otus;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.repository.executor.DbExecutorImpl;
import ru.otus.core.sessionmanager.TransactionRunnerJdbc;
import ru.otus.crm.datasource.DriverManagerDataSource;
import ru.otus.crm.model.Client;
import ru.otus.crm.service.DbServiceClientImpl;
import ru.otus.jdbc.mapper.*;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"java:S125", "java:S1481"})
public class HomeWork {
    private static final String URL = "jdbc:postgresql://localhost:5430/demoDB";
    private static final String USER = "usr";
    private static final String PASSWORD = "pwd";

    private static final Logger log = LoggerFactory.getLogger(HomeWork.class);

    public static void main(String[] args) {
        var dataSource = new DriverManagerDataSource(URL, USER, PASSWORD);
        flywayMigrations(dataSource);
        var transactionRunner = new TransactionRunnerJdbc(dataSource);
        var dbExecutor = new DbExecutorImpl();

        EntityClassMetaData<Client> entityClassMetaDataClient = new EntityClassMetaDataImpl<>(Client.class);
        EntitySQLMetaData entitySQLMetaDataClient = new EntitySQLMetaDataImpl(entityClassMetaDataClient);
        var dataTemplateClient = new DataTemplateJdbc<Client>(
                dbExecutor, entitySQLMetaDataClient, entityClassMetaDataClient);

        var dbServiceClient = new DbServiceClientImpl(transactionRunner, dataTemplateClient);


        testCachePerformance(dbServiceClient);


    }

    private static void testCachePerformance(DbServiceClientImpl dbServiceClient) {
        log.info("=== Cache Performance Test ===");


        List<Long> clientIds = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Client client = dbServiceClient.saveClient(new Client("PerformanceTest_" + i));
            clientIds.add(client.getId());
            // Ensure they are cached by getting them once
            dbServiceClient.getClient(client.getId());
        }
        log.info("Created and cached 10 clients");


        long cacheStartTime = System.nanoTime();
        for (Long clientId : clientIds) {
            Client client = dbServiceClient.getClient(clientId)
                    .orElseThrow(() -> new RuntimeException("Client not found"));
        }
        long cacheTime = System.nanoTime() - cacheStartTime;


        log.info("Clearing cache with GC...");
        System.gc();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }


        long dbStartTime = System.nanoTime();
        for (Long clientId : clientIds) {
            Client client = dbServiceClient.getClient(clientId)
                    .orElseThrow(() -> new RuntimeException("Client not found"));
        }
        long dbTime = System.nanoTime() - dbStartTime;


        log.info("Time to get 10 clients from CACHE: {} ns", cacheTime);
        log.info("Time to get 10 clients from DATABASE: {} ns", dbTime);
        log.info("Cache is {} times faster", (double) dbTime / cacheTime);


        log.info("Average per client - Cache: {} ns, Database: {} ns",
                cacheTime / 10, dbTime / 10);
    }

    private static void flywayMigrations(DataSource dataSource) {
        log.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
        log.info("db migration finished.");
        log.info("***");
    }
}
