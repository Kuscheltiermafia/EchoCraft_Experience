package club.selbsthilfe.kuscheltiermafia.echoCraft_Lobby;

import club.selbsthilfe.kuscheltiermafia.echoCraft_Lobby.events.JoinEvent;
import club.selbsthilfe.kuscheltiermafia.echoCraft_Lobby.events.LeaveEvent;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationStore;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public final class EchoCraft_Lobby extends JavaPlugin {

    @Override
    public void onEnable() {
        this.getLogger().info("EchoCraft_Lobby enabled! Beep boop beep beep boop!");

        registerTranslations();

        PluginManager pluginManager = getServer().getPluginManager();

        pluginManager.registerEvents(new JoinEvent(), this);
        pluginManager.registerEvents(new LeaveEvent(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public Plugin getPlugin(){
        return this;
    }

    private void registerTranslations(){
        TranslationStore.StringBased<MessageFormat> store = TranslationStore.messageFormat(Key.key("echocraft.lang"));

        ResourceBundle bundleUS = ResourceBundle.getBundle("echocraft.lang.messages", Locale.US);
        store.registerAll(Locale.US, bundleUS, true);
        ResourceBundle bundleGE = ResourceBundle.getBundle("echocraft.lang.messages", Locale.GERMANY);
        store.registerAll(Locale.GERMANY, bundleGE, true);

        GlobalTranslator.translator().addSource(store);
    }
}
