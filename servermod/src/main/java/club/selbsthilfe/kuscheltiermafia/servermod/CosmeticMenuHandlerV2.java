package club.selbsthilfe.kuscheltiermafia.servermod;

import club.selbsthilfe.kuscheltiermafia.CosmeticRepository;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;

import java.sql.SQLException;
import java.util.HashMap;
import static club.selbsthilfe.kuscheltiermafia.Configuration.cosmeticinventorysize;


public class CosmeticMenuHandlerV2 {
    private static final int COSMETICS_PER_PAGE = cosmeticinventorysize * 9;


    private final CosmeticRepository cosmeticRepository;
    private HashMap<ServerPlayer, HashMap<String, Boolean>> cosmeticManagerV2;

    public CosmeticMenuHandlerV2(CosmeticRepository cosmeticRepository, HashMap<ServerPlayer, HashMap<String, Boolean>> cosmeticManagerV2) {
        this.cosmeticRepository = cosmeticRepository;
        this.cosmeticManagerV2 = cosmeticManagerV2;
    }

    public void open(ServerPlayer player){
        HashMap<String, Boolean> entries = loadCosmetics(player);
        cosmeticManagerV2.put(player, entries);
        player.openMenu(new SimpleMenuProvider(
                (syncId, inventory, ignored) -> new CosmeticMenuV2(syncId, inventory, player, entries, COSMETICS_PER_PAGE),
                Component.literal("Placeholder")
        ));
    }

    public HashMap<String, Boolean> loadCosmetics(ServerPlayer player) {
        try {
            return cosmeticRepository.loadWardrobe(player.getUUID());
        } catch (SQLException e) {
            player.sendSystemMessage(Component.literal("Placeholder"));
            System.err.println("Could not load cosmetics for " + player.getName());
            e.printStackTrace();
        }
        return null;
    }



}
