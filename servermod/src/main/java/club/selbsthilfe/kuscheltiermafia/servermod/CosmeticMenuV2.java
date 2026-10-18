package club.selbsthilfe.kuscheltiermafia.servermod;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import static club.selbsthilfe.kuscheltiermafia.Configuration.cosmeticinventorysize;
import java.util.HashMap;

public class CosmeticMenuV2 extends AbstractContainerMenu {
    private final ServerPlayer player;
    private final SimpleContainer container;
    private final HashMap<String, Boolean> entries;
    public CosmeticMenuV2(int syncId, Inventory inventory, ServerPlayer player, HashMap<String, Boolean> entries, int cosmenticsperpage) {
        super(MenuType.GENERIC_9x3, syncId);
        this.player = player;
        this.container = new SimpleContainer(cosmenticsperpage);
        this.entries = entries;
        container.startOpen(player);
        for (int row = 0; row < cosmeticinventorysize; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(container, column + row * 9, 8 + column * 18, 18 + row * 18));
            }
        }
        addStandardInventorySlots(inventory, 8, 85);


    }

}
