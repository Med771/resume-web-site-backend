package ru.ai.sin;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Поднимает контекст Spring с реальным PostgreSQL в Docker (Flyway + JPA validate).
 * <p>С запущенным Docker тест выполняется. Без Docker — класс помечается как <em>Disabled</em>,
 * {@code mvn test} остаётся зелёным (удобно для машин без Docker / агентов без сокета).
 * В CI с Docker этот тест должен реально выполняться.
 */
@SpringBootTest
@Testcontainers(disabledWithoutDocker = true)
class ResumeWebSiteBackendApplicationTests {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("resume_test");

    @DynamicPropertySource
    static void registerDatasource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void contextLoads() {
    }
}
