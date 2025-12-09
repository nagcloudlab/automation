package com.upi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

/**
 * Transfer Service Application
 * 
 * A Spring Boot REST API service for banking operations including:
 * - Account Management (CRUD operations)
 * - Fund Transfers between accounts
 * - Transaction History and Audit Trail
 * 
 * This application is designed for REST Assured testing training.
 * 
 * Profiles:
 * - h2: In-memory H2 database (default, for development/testing)
 * - postgresql: PostgreSQL database (for production)
 * 
 * Usage:
 * - H2 Profile: mvn spring-boot:run (or java -jar transfer-service.jar)
 * - PostgreSQL Profile: mvn spring-boot:run -Dspring.profiles.active=postgresql
 * 
 * H2 Console: http://localhost:8080/h2-console (when using h2 profile)
 * 
 * @author Training Team
 * @version 1.0.0
 */
@SpringBootApplication
@Slf4j
public class TransferServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TransferServiceApplication.class, args);
        log.info("========================================");
        log.info("Transfer Service Application Started!");
        log.info("========================================");
        log.info("");
        log.info("📚 API DOCUMENTATION (Swagger UI):");
        log.info("   http://localhost:8080/swagger-ui.html");
        log.info("");
        log.info("📋 OpenAPI JSON:");
        log.info("   http://localhost:8080/api-docs");
        log.info("");
        log.info("🗄️  H2 Database Console:");
        log.info("   http://localhost:8080/h2-console");
        log.info("   JDBC URL: jdbc:h2:mem:transferdb");
        log.info("   Username: sa | Password: (empty)");
        log.info("");
        log.info("🔗 API Endpoints:");
        log.info("   Accounts:     GET/POST http://localhost:8080/api/v1/accounts");
        log.info("   Transfers:    POST http://localhost:8080/api/v1/transfers");
        log.info("   Transactions: GET http://localhost:8080/api/v1/transactions");
        log.info("   Health:       GET http://localhost:8080/actuator/health");
        log.info("========================================");
    }
}
