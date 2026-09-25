package club.selbsthilfe.kuscheltiermafia;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationStore;
import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

public class Configuration {

    public static void registerTranslations(){
        TranslationStore.StringBased<MessageFormat> store = TranslationStore.messageFormat(Key.key("echocraft.lang"));

        ResourceBundle bundleUS = ResourceBundle.getBundle("echocraft.lang.messages", Locale.US);
        store.registerAll(Locale.US, bundleUS, true);
        ResourceBundle bundleGE = ResourceBundle.getBundle("echocraft.lang.messages", Locale.GERMANY);
        store.registerAll(Locale.GERMANY, bundleGE, true);

        GlobalTranslator.translator().addSource(store);
    }

    public static Connection getConnection(Plugin plugin) throws SQLException {
        String host = plugin.getConfig().getString("host");
        String port = plugin.getConfig().getString("port");
        String database = plugin.getConfig().getString("database");
        String username = plugin.getConfig().getString("username");
        String password = plugin.getConfig().getString("password");

        String url = "jdbc:postgresql://" + host + ":" + port + "/" + database;
        Properties props = new Properties();
        props.setProperty("username", username);
        props.setProperty("password", password);

        return DriverManager.getConnection(url, props);

    }

}
