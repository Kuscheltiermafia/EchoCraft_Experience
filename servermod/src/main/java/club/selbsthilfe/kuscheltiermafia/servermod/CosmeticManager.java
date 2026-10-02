package club.selbsthilfe.kuscheltiermafia.servermod;

import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;

public class CosmeticManager {
    public final ServerPlayer serverPlayer;
    public final List<String> cosmetics = new ArrayList<>();

    public CosmeticManager(ServerPlayer serverPlayer) {
        this.serverPlayer = serverPlayer;
    }
}
