package club.selbsthilfe.kuscheltiermafia.echoCraft_Lobby.mechanics;

import club.selbsthilfe.kuscheltiermafia.DatabaseManager;
import club.selbsthilfe.kuscheltiermafia.ec_db_migrations.MigrationService;
import club.selbsthilfe.kuscheltiermafia.echoCraft_Lobby.helper.EggHuntResult;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EggHuntTest {

    @Container
    private static final PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:17-alpine");

    private static DatabaseManager databaseManager;
    private static final YamlConfiguration egg_config = YamlConfiguration.loadConfiguration(
            new InputStreamReader(
                    Objects.requireNonNull(EggHuntTest.class.getClassLoader().getResourceAsStream("eggs.yaml")),
                    StandardCharsets.UTF_8
            )
    );

    @BeforeAll
    static void setup(){
        databaseManager = new DatabaseManager(
                postgreSQLContainer.getHost(),
                postgreSQLContainer.getFirstMappedPort().toString(),
                postgreSQLContainer.getDatabaseName(),
                postgreSQLContainer.getUsername(),
                postgreSQLContainer.getPassword()
        );

        MigrationService.migrate(new String[]{postgreSQLContainer.getJdbcUrl(), postgreSQLContainer.getUsername(), postgreSQLContainer.getPassword()});
    }

    @ParameterizedTest
    @CsvSource({
            "1, 89037f95-2947-4109-9316-1e2a38a86433, spooky26, 1",
            "1, 89037f95-2947-4109-9316-1e2a38a86433, spooky26, 2",
            "1, 89037f95-2947-4109-9316-1e2a38a86433, christmas26, 2",
            "1, b4ca1993-88a1-4461-8cd4-bd617ea273e4, spooky26, 1",
            "1, b4ca1993-88a1-4461-8cd4-bd617ea273e4, christmas26, 10",
            "0, 89037f95-2947-4109-9316-1e2a38a86433, spooky26, 1",
            "0, b4ca1993-88a1-4461-8cd4-bd617ea273e4, christmas26, 10",
    })
    @Order(1)
    void testRegisterEgg(int expected, UUID uuid, String event, float egg_id){
        assertEquals(expected, EggHunt.registerEgg(databaseManager, uuid, event, egg_id));
    }

    @ParameterizedTest
    @CsvSource({
            "89037f95-2947-4109-9316-1e2a38a86433, spooky26, 2",
            "b4ca1993-88a1-4461-8cd4-bd617ea273e4, spooky26, 1"
    })
    @Order(2)
    void testEggCount(UUID uuid, String event, int amount){
        EggHuntResult result = EggHunt.getTotalEggsFound(databaseManager, egg_config, uuid, event);
        assertEquals(amount, result.found());
    }

    @ParameterizedTest
    @CsvSource({
            "89037f95-2947-4109-9316-1e2a38a86433, spooky26, 10"
    })
    @Order(2)
    void testConfig(UUID uuid, String event, int amount){
        EggHuntResult result = EggHunt.getTotalEggsFound(databaseManager, egg_config, uuid, event);
        assertEquals(amount, result.available());
    }

    @ParameterizedTest
    @CsvSource({
            "89037f95-2947-4109-9316-1e2a38a86433, spooky26"
    })
    void testGiveReward(UUID uuid, String event){
        assertEquals(1, EggHunt.giveReward(uuid, event, databaseManager, egg_config));
    }

}
