package club.selbsthilfe.kuscheltiermafia.echoCraft_Lobby.events;

import org.bukkit.block.data.type.Door;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractEvent implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        try {
            assert event.getClickedBlock() != null;
            if (event.getClickedBlock().getBlockData() instanceof Door) return;
            event.setCancelled(true);
        }catch (NullPointerException ignored){}
    }

}
