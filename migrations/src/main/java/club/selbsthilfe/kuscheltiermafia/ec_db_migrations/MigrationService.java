package club.selbsthilfe.kuscheltiermafia.ec_db_migrations;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.output.MigrateResult;

public class MigrationService {

    static void main(String[] args){
        migrate(args);
    }

    static MigrateResult migrate(String[] args) {

        if (args.length != 3) {
            System.out.println("Please provide url, user and password as arguments in this order");
        }

        String url = args[0];
        String user = args[1];
        String password = args[2];

        Flyway flyway = Flyway.configure().dataSource(url, user, password).load();

        return flyway.migrate();
    }
}
