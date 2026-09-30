package club.selbsthilfe.kuscheltiermafia.echoCraft_Lobby;

import club.selbsthilfe.kuscheltiermafia.Configuration;
import club.selbsthilfe.kuscheltiermafia.DatabaseManager;
import club.selbsthilfe.kuscheltiermafia.echoCraft_Lobby.events.InteractEntityEvent;
import club.selbsthilfe.kuscheltiermafia.echoCraft_Lobby.events.InteractEvent;
import club.selbsthilfe.kuscheltiermafia.echoCraft_Lobby.events.JoinEvent;
import club.selbsthilfe.kuscheltiermafia.echoCraft_Lobby.events.LeaveEvent;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class EchoCraft_Lobby extends JavaPlugin {

    @Override
    public void onEnable() {
        this.getLogger().info("EchoCraft_Lobby enabled! Beep boop beep beep boop!");

        Configuration.registerTranslations();
        DatabaseManager databaseManager = new DatabaseManager(getConfig().getString("host"), getConfig().getString("port"), getConfig().getString("database"), getConfig().getString("username"), getConfig().getString("password"));

        saveDefaultConfig();
        File egg_config_file = new File(this.getDataFolder(), "eggs.yaml");
        YamlConfiguration egg_config = YamlConfiguration.loadConfiguration(egg_config_file);

        PluginManager pluginManager = getServer().getPluginManager();

        pluginManager.registerEvents(new JoinEvent(this), this);
        pluginManager.registerEvents(new LeaveEvent(), this);
        pluginManager.registerEvents(new InteractEvent(), this);
        pluginManager.registerEvents(new InteractEntityEvent(databaseManager, egg_config), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
