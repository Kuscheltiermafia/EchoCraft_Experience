package club.selbsthilfe.kuscheltiermafia.echoCraft_Lobby.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class HungerEvent implements Listener {
    @EventHandler
    public void onHunger(FoodLevelChangeEvent event){
        event.setFoodLevel(20);
    }
}
