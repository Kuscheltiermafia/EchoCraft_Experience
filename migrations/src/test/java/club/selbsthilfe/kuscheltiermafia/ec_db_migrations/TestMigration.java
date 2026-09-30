package club.selbsthilfe.kuscheltiermafia.ec_db_migrations;

import org.flywaydb.core.api.output.MigrateResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

@Testcontainers
public class TestMigration {

    @Container
    private static final PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:18-alpine");

    @Test
    public void migrateDatabase() {
        MigrateResult result = MigrationService.migrate(new String[]{postgreSQLContainer.getJdbcUrl(), postgreSQLContainer.getUsername(), postgreSQLContainer.getPassword()});

        Assertions.assertTrue(result.success, "Migration result reports success");
        Assertions.assertTrue(result.migrationsExecuted > 0, "Executed >0 migrations");
    }

}
