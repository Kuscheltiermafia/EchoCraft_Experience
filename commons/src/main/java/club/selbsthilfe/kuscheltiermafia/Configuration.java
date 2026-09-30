package club.selbsthilfe.kuscheltiermafia;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationStore;

import java.text.MessageFormat;
import java.util.Locale;
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

}
