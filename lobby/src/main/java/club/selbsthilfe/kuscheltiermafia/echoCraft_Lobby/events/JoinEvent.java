package club.selbsthilfe.kuscheltiermafia.echoCraft_Lobby.events;

import club.selbsthilfe.kuscheltiermafia.echoCraft_Lobby.EchoCraft_Lobby;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvent implements Listener {

    private final EchoCraft_Lobby plugin;

    public JoinEvent (EchoCraft_Lobby plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        World world = player.getWorld();

        Component joinMessage = Component.translatable()
                .key("player.event.join")
                .color(NamedTextColor.YELLOW)
                .fallback("{0} has joined the lobby")
                .arguments(player.displayName())
                .build();
        event.joinMessage(joinMessage);

        String spawn_point_tag = plugin.getConfig().getString("entity-tags.spawn", "spawn_point");
        Component noSpawnpoint = Component.translatable()
                .key("error.NoSpawnPoint")
                .fallback("No spawn point found. Please contact the server administrator!")
                .build();
        world.getEntities().stream()
                .filter(entity -> entity.getScoreboardTags().contains(spawn_point_tag))
                .findFirst()
                .ifPresentOrElse(
                        spawn_marker -> player.teleport(spawn_marker.getLocation()),
                        () -> player.sendMessage(noSpawnpoint)
                )
        ;

        Particle.SCULK_CHARGE.builder()     //Gerne verbessern. Hab nur das alte übernommen
                .location(player.getLocation().add(0, 1, 0))
                .offset(0.2, 1, 0.2)
                .receivers(32, false)
                .count(100)
                .extra(0.03)
                .data((float) Math.toRadians(0))
                .spawn();


    }
}
