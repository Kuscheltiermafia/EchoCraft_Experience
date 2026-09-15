package club.selbsthilfe.kuscheltiermafia.echoCraft_Lobby.events;

import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveEvent implements Listener {

    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        event.quitMessage(Component.empty());
    }
}
