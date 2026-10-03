package club.selbsthilfe.kuscheltiermafia.echoCraft_Lobby.events;

import club.selbsthilfe.kuscheltiermafia.DatabaseManager;
import club.selbsthilfe.kuscheltiermafia.echoCraft_Lobby.mechanics.EggHunt;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.Set;

public class InteractEntityEvent implements Listener {

    private static DatabaseManager databaseManager = null;
    private static YamlConfiguration egg_config = null;

    public InteractEntityEvent(DatabaseManager databaseManager, YamlConfiguration egg_config) {
        InteractEntityEvent.databaseManager = databaseManager;
        InteractEntityEvent.egg_config = egg_config;
    }

    @EventHandler
    public static void onInteractEntity(PlayerInteractEntityEvent event) {

        Player player = event.getPlayer();
        Entity entity = player.getTargetEntity(3);

        if (entity == null) return;

        Set<String> tags = entity.getScoreboardTags();
        if (tags.contains("egg")){

            club.selbsthilfe.kuscheltiermafia.echoCraft_Lobby.helper.EggHuntResult eggHuntResult = EggHunt.processEgg(tags, player.getUniqueId(), databaseManager, egg_config);

            if (eggHuntResult.isNew()) {
                player.sendMessage(Component.translatable()
                        .key("egghunt.foundEgg")
                        .fallback("You found a secret!")
                        .build())
                ;
            } else {
                player.sendMessage(Component.translatable()
                        .key("egghunt.alreadyFound")
                        .fallback("You already found this secret.")
                        .build())
                ;
            }
            player.sendMessage(Component.translatable()
                    .key("egghunt.found")
                    .fallback("You now have {0} out of {1} secrets for this event.")
                    .arguments(Component.text(eggHuntResult.found()), Component.text(eggHuntResult.available()))
                    .build());

            if (eggHuntResult.found() == eggHuntResult.available() && eggHuntResult.new_cosmetic()) {
                player.sendMessage(Component.translatable()
                        .key("cosmetic.new")
                        .fallback("You got a new cosmetic. Check out the menu to learn more!")
                        .build());
            }

            return; //FFU
        }



    }

}
