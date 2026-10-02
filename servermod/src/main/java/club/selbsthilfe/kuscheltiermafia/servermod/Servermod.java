package club.selbsthilfe.kuscheltiermafia.servermod;

import club.selbsthilfe.kuscheltiermafia.servermod.configs.Defaultconfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import club.selbsthilfe.kuscheltiermafia.Configuration;
import club.selbsthilfe.kuscheltiermafia.DatabaseManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;

public class Servermod implements ModInitializer {



    DatabaseManager databaseManager;

    @Override
    public void onInitialize() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
                spawnParticles(handler.player));
        ConfigManager.load();

        databaseManager = new DatabaseManager(ConfigManager.config.Host , ConfigManager.config.Port, ConfigManager.config.Database, ConfigManager.config.Username, ConfigManager.config.Password);
        try (Connection connection = databaseManager.getConnection()) {
            System.out.println("Connected to PostgreSQL!");
        } catch (SQLException e) {
            System.err.println("Could not connect to PostgreSQL:");
            e.printStackTrace();
        }

    }




    private void spawnParticles(ServerPlayer player) {
        ServerLevel world = (ServerLevel) player.level();

        world.sendParticles(
                ParticleTypes.FLAME,
                player.getX(),
                player.getY() + 1.0,
                player.getZ(),
                3000,
                0.5,
                1.0,
                0.5,
                0.05
        );
    }
}
