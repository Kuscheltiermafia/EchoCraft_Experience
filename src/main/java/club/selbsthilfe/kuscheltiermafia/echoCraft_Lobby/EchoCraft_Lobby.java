package club.selbsthilfe.kuscheltiermafia.echoCraft_Lobby;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class EchoCraft_Lobby extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getLogger().info("EchoCraft_Lobby enabled! Beep boop beep beep boop!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
