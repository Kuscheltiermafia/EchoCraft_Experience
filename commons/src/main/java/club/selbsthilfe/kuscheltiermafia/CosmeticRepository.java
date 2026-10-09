package club.selbsthilfe.kuscheltiermafia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CosmeticRepository {
    private final DatabaseManager databaseManager;

    public CosmeticRepository(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public List<CosmeticEntry> loadWardrobe(UUID playerUuid) throws SQLException {
        List<CosmeticEntry> cosmetics = new ArrayList<>();
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT cosmetic_key, is_equipped FROM wardrobe WHERE player_uuid = ? ORDER BY cosmetic_key")) {
            statement.setObject(1, playerUuid);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    cosmetics.add(new CosmeticEntry(
                            resultSet.getString("cosmetic_key"),
                            resultSet.getBoolean("is_equipped")
                    ));
                }
            }
        }
        return cosmetics;
    }

    public List<String> loadEquippedKeys(UUID playerUuid) throws SQLException {
        return loadWardrobe(playerUuid).stream()
                .filter(CosmeticEntry::equipped)
                .map(CosmeticEntry::key)
                .toList();
    }

    public boolean setEquipped(UUID playerUuid, String cosmeticKey, boolean equipped) throws SQLException {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE wardrobe SET is_equipped = ? WHERE player_uuid = ? AND cosmetic_key = ?")) {
            statement.setBoolean(1, equipped);
            statement.setObject(2, playerUuid);
            statement.setString(3, cosmeticKey);
            return statement.executeUpdate() > 0;
        }
    }
}
