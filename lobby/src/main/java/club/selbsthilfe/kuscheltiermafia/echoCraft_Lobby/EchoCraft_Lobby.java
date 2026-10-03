package club.selbsthilfe.kuscheltiermafia.echoCraft_Lobby;

import club.selbsthilfe.kuscheltiermafia.Configuration;
import club.selbsthilfe.kuscheltiermafia.DatabaseManager;
import club.selbsthilfe.kuscheltiermafia.echoCraft_Lobby.events.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public final class EchoCraft_Lobby extends JavaPlugin {

    @Override
    public void onEnable() {
        this.getLogger().info("EchoCraft_Lobby enabled! Beep boop beep beep boop!");

        saveDefaultConfig();
        final YamlConfiguration egg_config = YamlConfiguration.loadConfiguration(
                new InputStreamReader(
                        Objects.requireNonNull(EchoCraft_Lobby.class.getClassLoader().getResourceAsStream("eggs.yaml")),
                        StandardCharsets.UTF_8
                )
        );

        Configuration.registerTranslations();
        DatabaseManager databaseManager = new DatabaseManager(getConfig().getString("database.host"), getConfig().getString("database.port"), getConfig().getString("database.database"), getConfig().getString("database.username"), getConfig().getString("database.password"));

        PluginManager pluginManager = getServer().getPluginManager();

        pluginManager.registerEvents(new JoinEvent(this), this);
        pluginManager.registerEvents(new LeaveEvent(), this);
        pluginManager.registerEvents(new InteractEvent(), this);
        pluginManager.registerEvents(new InteractEntityEvent(databaseManager, egg_config), this);
        pluginManager.registerEvents(new DamageEvent(), this);
        pluginManager.registerEvents(new HungerEvent(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
