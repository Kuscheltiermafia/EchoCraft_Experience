package club.selbsthilfe.kuscheltiermafia.echoCraft_Lobby.mechanics;

import club.selbsthilfe.kuscheltiermafia.DatabaseManager;
import club.selbsthilfe.kuscheltiermafia.Utils;
import club.selbsthilfe.kuscheltiermafia.echoCraft_Lobby.helper.EggHuntResult;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.VisibleForTesting;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import java.util.UUID;

public class EggHunt {

    public static EggHuntResult processEgg(Set<String> tags, UUID uuid, DatabaseManager databaseManager, YamlConfiguration egg_config) {

        String event = Utils.getTag(tags, "event");
        float id = Float.parseFloat(Utils.getTag(tags, "id"));

        int success = registerEgg(databaseManager, uuid, event, id);

        EggHuntResult eggsFound = getTotalEggsFound(databaseManager, egg_config, uuid, event);

        int found = eggsFound.found();
        int available = eggsFound.available();


        if (success == 1){
            return new EggHuntResult(true, found, available, eggsFound.new_cosmetic());
        }else{
            return new EggHuntResult(false, found, available, eggsFound.new_cosmetic());
        }
    }

    @VisibleForTesting
    static int giveReward(UUID playerID, String event, DatabaseManager databaseManager, YamlConfiguration egg_config){

        try (Connection connection = databaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement("INSERT INTO wardrobe (player_uuid, cosmetic_key) VALUES (?, ?);")) {
            String cosmetic_key = egg_config.getString(event + ".reward");

            stmt.setObject(1, playerID);
            stmt.setString(2, cosmetic_key);

            return stmt.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getPlayer(playerID).sendMessage(
                    Component.translatable()
                            .key("cosmetic.finished")
                            .fallback("§8You already finished this event!")
                            .build()
            );
            return 0;
        }

    }

    @VisibleForTesting
    static int registerEgg(DatabaseManager databaseManager, UUID uuid, String event, Float id){
        String sql_set = "INSERT INTO egghunt (player_uuid, event, egg_id) VALUES (?, ?, ?) ON CONFLICT DO NOTHING;";

        try (Connection connection = databaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql_set)) {

            stmt.setObject(1, uuid);
            stmt.setString(2, event);
            stmt.setFloat(3, id);

            return stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @VisibleForTesting
    static EggHuntResult getTotalEggsFound(DatabaseManager databaseManager, YamlConfiguration egg_config, UUID uuid, String event){
        int found = 0;
        int available = 0;
        int new_cos = 0;

        String sql_get = "SELECT COUNT(*) FROM egghunt WHERE player_uuid = ? AND event = ?;";

        try (Connection connection = databaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql_get)) {

            stmt.setObject(1, uuid);
            stmt.setString(2, event);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    found = rs.getInt(1);
                    available = egg_config.getStringList(event + ".egg_ids").size();

                    if (found >= available){
                        new_cos = giveReward(uuid, event, databaseManager, egg_config);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (new_cos == 1) {
            return new EggHuntResult(true, found, available, true);
        }else {
            return new EggHuntResult(true, found, available, false);
        }
    }

}

