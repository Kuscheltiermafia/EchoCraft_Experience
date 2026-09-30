package club.selbsthilfe.kuscheltiermafia.echoCraft_Lobby.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractEvent implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        event.setCancelled(true);
    }

}
