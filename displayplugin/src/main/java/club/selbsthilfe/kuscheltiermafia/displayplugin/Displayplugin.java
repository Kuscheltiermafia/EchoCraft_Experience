package club.selbsthilfe.kuscheltiermafia.displayplugin;

import club.selbsthilfe.kuscheltiermafia.displayplugin.events.Join;
import club.selbsthilfe.kuscheltiermafia.displayplugin.events.Left;
import club.selbsthilfe.kuscheltiermafia.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class Displayplugin extends JavaPlugin {

    public DatabaseManager databaseManager;
    public final Map<UUID, CosmeticManager> cosmeticManagers = new HashMap<>();
    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        databaseManager = new DatabaseManager(getConfig().getString("database.host"), getConfig().getString("database.port"), getConfig().getString("database.database"), getConfig().getString("database.username"), getConfig().getString("database.password"));

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new Join(this), this);
        pluginManager.registerEvents(new Left(this), this);
        startCosmeticTick();

        registerCommand("test", (source, args) -> {
            source.getSender().sendMessage("Hello!");
            CosmeticManager cosmeticManager = new CosmeticManager(Bukkit.getPlayer(source.getSender().getName()));
            //cosmeticManager.cosmetics.add("flame");
            cosmeticManager.cosmetics.add("soultrails");
            cosmeticManagers.put(Bukkit.getPlayer(source.getSender().getName()).getUniqueId(), cosmeticManager);
        });
    }

    public void startCosmeticTick(){
        new BukkitRunnable(){
            @Override
            public void run() {
                for (CosmeticManager cosmeticManager : cosmeticManagers.values()) {
                    tickEquippedCosmetics(cosmeticManager);
                }
            }
        }.runTaskTimer(Bukkit.getPluginManager().getPlugin("Displayplugin"), 0L, 1L);
    }

    public void tickEquippedCosmetics(CosmeticManager cosmeticManager) {
        for (String cosmeticKey : cosmeticManager.cosmetics) {
            if ("soultrails".equals(cosmeticKey)) {
                spawnParticles(cosmeticManager.serverPlayer, Particle.SOUL, cosmeticManager.serverPlayer.getX(), cosmeticManager.serverPlayer.getY(), cosmeticManager.serverPlayer.getZ(), 2, 0.01f,0.005f,0.01f, 0.02f);
            }
        }
    }

    public void spawnParticles(Player player, Particle particle, double x, double y, double z, int count, float xDist, float yDist, float zDist, float speed){
        player.getWorld().spawnParticle(particle, x,y,z,count,xDist,yDist,zDist,speed);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
