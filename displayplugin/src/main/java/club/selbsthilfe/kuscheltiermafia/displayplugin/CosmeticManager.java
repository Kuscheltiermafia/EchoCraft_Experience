package club.selbsthilfe.kuscheltiermafia.displayplugin;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CosmeticManager {
    public final Player serverPlayer;
    public final List<String> cosmetics = new ArrayList<>();

    public CosmeticManager(Player serverPlayer) {
        this.serverPlayer = serverPlayer;
    }
}
