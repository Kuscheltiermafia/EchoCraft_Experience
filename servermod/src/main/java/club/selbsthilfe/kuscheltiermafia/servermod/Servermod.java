package club.selbsthilfe.kuscheltiermafia.servermod;

import com.mojang.brigadier.Command;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.commands.Commands;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import club.selbsthilfe.kuscheltiermafia.DatabaseManager;
import net.minecraft.world.effect.MobEffect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Servermod implements ModInitializer {
    private int ticks = 0;
    DatabaseManager databaseManager;
    private final Map<UUID, CosmeticManager> cosmeticManagers = new HashMap<>();

    @Override
    public void onInitialize() {
        ConfigManager.load();

        databaseManager = new DatabaseManager(ConfigManager.config.Host , ConfigManager.config.Port, ConfigManager.config.Database, ConfigManager.config.Username, ConfigManager.config.Password);
        try (Connection connection = databaseManager.getConnection()) {
            System.out.println("Connected to PostgreSQL!");
        } catch (SQLException e) {
            System.err.println("Could not connect to PostgreSQL:");
            e.printStackTrace();
        }

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
                join(handler.player, server));
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) ->
                cosmeticManagers.remove(handler.player.getUUID()));
        CommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess, environment) -> {
                    dispatcher.register(
                            Commands.literal("hello")
                                    .executes(context -> {
                                        context.getSource().sendSuccess(
                                                () -> Component.literal("Hello!"),
                                                false
                                        );
                                        CosmeticManager cosmeticManager = new CosmeticManager(context.getSource().getPlayer());
                                        //cosmeticManager.cosmetics.add("flame");
                                        cosmeticManager.cosmetics.add("soultrails");
                                        cosmeticManagers.put(context.getSource().getPlayer().getUUID(), cosmeticManager);


                                        return Command.SINGLE_SUCCESS;
                                    })
                    );
                }
        );

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            //ticks++;

            // Every 5 ticks = 0.25 seconds
            //if (ticks < 5) {
            //    return;
            //}

            //ticks = 0;

            for (CosmeticManager cosmeticManager : cosmeticManagers.values()) {
                tickEquippedCosmetics(cosmeticManager);
            }
        });

    }


    private void join(ServerPlayer player, MinecraftServer server){
        CosmeticManager cosmeticManager = new CosmeticManager(player, server);

        try (Connection connection = databaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT cosmetic_key FROM wardrobe WHERE player_uuid = ? AND is_equipped")) {
            statement.setObject(1, player.getUUID());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    cosmeticManager.cosmetics.add(resultSet.getString("cosmetic_key"));
                }
            }
            cosmeticManagers.put(player.getUUID(), cosmeticManager);
        } catch (SQLException e) {
            System.err.println("Could not load equipped cosmetics for " + player.getName());
            e.printStackTrace();
        }
    }

    private void tickEquippedCosmetics(CosmeticManager cosmeticManager) {
        for (String cosmeticKey : cosmeticManager.cosmetics) {
            if ("soultrails".equals(cosmeticKey)) {
                if (cosmeticManager.serverPlayer.isInvisible()) {
                    return;
                } else if (!cosmeticManager.serverPlayer.gameMode().isSurvival() && !cosmeticManager.serverPlayer.gameMode().isCreative()) {

                    return;
                }else{
                    spawnParticles(cosmeticManager.serverPlayer, ParticleTypes.SOUL, cosmeticManager.serverPlayer.getX(), cosmeticManager.serverPlayer.getY(), cosmeticManager.serverPlayer.getZ(), 2, 0.01f, 0.005f, 0.01f, 0.02f);
                }
            }
        }
    }

    private void spawnParticles(ServerPlayer player, SimpleParticleType particleTypes,double x, double y, double z, int count, float xDist, float yDist, float zDist, float speed) {
        ServerLevel world = (ServerLevel) player.level();

        world.sendParticles(
                particleTypes,
                x,
                y,
                z,
                count,
                xDist,
                yDist,
                zDist,
                speed
        );
    }
}
