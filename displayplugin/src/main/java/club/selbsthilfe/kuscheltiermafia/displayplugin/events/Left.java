package club.selbsthilfe.kuscheltiermafia.displayplugin.events;

import club.selbsthilfe.kuscheltiermafia.displayplugin.Displayplugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class Left implements Listener {
    private Displayplugin plugin;

    public Left(Displayplugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    private void LeftEvent(PlayerQuitEvent e){
        plugin.cosmeticManagers.remove(e.getPlayer().getUniqueId());
    }
}
