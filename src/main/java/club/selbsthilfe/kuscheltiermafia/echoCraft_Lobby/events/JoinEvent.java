package club.selbsthilfe.kuscheltiermafia.echoCraft_Lobby.events;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvent implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();

        Component joinMessage = Component.translatable()
                .key("player.event.join")
                .fallback("%s has joined the lobby")
                .arguments(player.displayName())
                .build();
        event.joinMessage(joinMessage);

        for (Entity entity : world.getEntitiesByClass(Interaction.class)) {
            if (entity.getScoreboardTags().contains("spawn_point")) {
                Location spawnLocation = entity.getLocation();
                player.teleport(spawnLocation);
            }
        }

    }
}
