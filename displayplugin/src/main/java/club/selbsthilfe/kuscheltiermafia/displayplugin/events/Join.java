package club.selbsthilfe.kuscheltiermafia.displayplugin.events;

import club.selbsthilfe.kuscheltiermafia.displayplugin.CosmeticManager;
import club.selbsthilfe.kuscheltiermafia.displayplugin.Displayplugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;

public class Join implements Listener {
    private Displayplugin plugin;

    public Join(Displayplugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    private void JoinEvent(PlayerJoinEvent e){
        Player player = e.getPlayer();
        CosmeticManager cosmeticManager = new CosmeticManager(player);

        try {
            cosmeticManager.cosmetics.addAll(plugin.cosmeticRepository.loadEquippedKeys(player.getUniqueId()));
            plugin.cosmeticManagers.put(player.getUniqueId(), cosmeticManager);
        } catch (SQLException ee) {
            System.err.println("Could not load equipped cosmetics for " + player.getName());
            ee.printStackTrace();
        }
    }
}
