package club.selbsthilfe.kuscheltiermafia.echoCraft_Lobby;

import club.selbsthilfe.kuscheltiermafia.Configuration;
import club.selbsthilfe.kuscheltiermafia.echoCraft_Lobby.events.InteractionEvent;
import club.selbsthilfe.kuscheltiermafia.echoCraft_Lobby.events.JoinEvent;
import club.selbsthilfe.kuscheltiermafia.echoCraft_Lobby.events.LeaveEvent;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationStore;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

public final class EchoCraft_Lobby extends JavaPlugin {

    Connection dbConn;

    @Override
    public void onEnable() {
        this.getLogger().info("EchoCraft_Lobby enabled! Beep boop beep beep boop!");

        saveDefaultConfig();
        Configuration.registerTranslations();

        PluginManager pluginManager = getServer().getPluginManager();

        pluginManager.registerEvents(new JoinEvent(this), this);
        pluginManager.registerEvents(new LeaveEvent(), this);
        pluginManager.registerEvents(new InteractionEvent(), this);



        try {
            dbConn = Configuration.getConnection(this);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public Connection getDbConn() {
        return dbConn;
    }
}
