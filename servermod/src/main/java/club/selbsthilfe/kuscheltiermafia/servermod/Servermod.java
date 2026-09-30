package club.selbsthilfe.kuscheltiermafia.servermod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;


public class Servermod implements ModInitializer {

    @Override
    public void onInitialize() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
                spawnParticles(handler.player));
        ServerP
    }

    private void spawnParticles(ServerPlayer player) {
        ServerLevel world = (ServerLevel) player.level();

        world.sendParticles(
                ParticleTypes.FLAME,
                player.getX(),
                player.getY() + 1.0,
                player.getZ(),
                30,
                0.5,
                1.0,
                0.5,
                0.05
        );
    }
}
