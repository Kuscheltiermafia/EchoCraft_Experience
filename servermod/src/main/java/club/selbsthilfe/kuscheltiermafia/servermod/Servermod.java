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
import club.selbsthilfe.kuscheltiermafia.CosmeticRepository;
import net.minecraft.world.effect.MobEffect;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Servermod implements ModInitializer {
    private int ticks = 0;
    DatabaseManager databaseManager;
    private CosmeticRepository cosmeticRepository;
    private final Map<UUID, CosmeticManager> cosmeticManagers = new HashMap<>();
    private CosmeticMenuHandler cosmeticMenuHandler;

    private HashMap<ServerPlayer, List<String>> cosmeticManagerV2 = new HashMap<>();

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

        cosmeticRepository = new CosmeticRepository(databaseManager);
        cosmeticMenuHandler = new CosmeticMenuHandler(cosmeticRepository, cosmeticManagers);

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
                join(handler.player));
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) ->
                 cosmeticManagerV2.remove(handler.player));

        CommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess, environment) -> {
                    dispatcher.register(
                            Commands.literal("cosmetics")
                                    .executes(context -> {
                                        ServerPlayer player = context.getSource().getPlayerOrException();
                                        cosmeticMenuHandler.open(player);
                                        return Command.SINGLE_SUCCESS;
                                    })
                    );
                }
        );

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayer player: cosmeticManagerV2.keySet()) {
                tickEquippedCosmeticsV2(cosmeticManagerV2, player);
            }
        });

    }


    private void join(ServerPlayer player){
        CosmeticManager cosmeticManager = new CosmeticManager(player);

        try {
            cosmeticManagerV2.put(player, cosmeticRepository.loadEquippedKeys(player.getUUID()));
            cosmeticManager.cosmetics.addAll(cosmeticRepository.loadEquippedKeys(player.getUUID()));
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
    private void tickEquippedCosmeticsV2(HashMap<ServerPlayer, List<String>> cosmeticManager, ServerPlayer player){
        for (String cosmeticKey : cosmeticManager.get(player)){
            if ("soultrails".equals(cosmeticKey)) {
                if (player.isInvisible()) {
                    return;
                } else if (!player.gameMode().isSurvival() && !player.gameMode().isCreative()) {
                    return;
                } else {
                    spawnParticles(player, ParticleTypes.SOUL, player.getX(), player.getY(), player.getZ(), 2, 0.01f, 0.005f, 0.01f, 0.02f);
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
