package club.selbsthilfe.kuscheltiermafia.servermod;

import club.selbsthilfe.kuscheltiermafia.CosmeticEntry;
import club.selbsthilfe.kuscheltiermafia.CosmeticRepository;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.SimpleMenuProvider;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CosmeticMenuHandler {
    private static final int ROWS = 3;
    private static final int COSMETICS_PER_PAGE = ROWS * 9;

    private final CosmeticRepository cosmeticRepository;
    private final Map<UUID, CosmeticManager> cosmeticManagers;

    public CosmeticMenuHandler(CosmeticRepository cosmeticRepository, Map<UUID, CosmeticManager> cosmeticManagers) {
        this.cosmeticRepository = cosmeticRepository;
        this.cosmeticManagers = cosmeticManagers;
    }

    public void open(ServerPlayer player) {
        List<CosmeticEntry> entries = loadCosmetics(player);
        player.openMenu(new SimpleMenuProvider(
                (syncId, inventory, ignored) -> new CosmeticMenu(syncId, inventory, player, entries),
                Component.literal("Cosmetics")
        ));
    }

    private List<CosmeticEntry> loadCosmetics(ServerPlayer player) {
        try {
            return cosmeticRepository.loadWardrobe(player.getUUID());
        } catch (SQLException e) {
            player.sendSystemMessage(Component.literal("Die Cosmetics konnten nicht geladen werden.").withStyle(ChatFormatting.RED));
            System.err.println("Could not load cosmetics for " + player.getName());
            e.printStackTrace();
        }
        return List.of();
    }

    private void setEquipped(ServerPlayer player, CosmeticEntry entry, boolean equipped) {
        try {
            cosmeticRepository.setEquipped(player.getUUID(), entry.key(), equipped);
            CosmeticManager manager = cosmeticManagers.get(player.getUUID());
            if (manager != null) {
                manager.cosmetics.remove(entry.key());
                if (equipped) manager.cosmetics.add(entry.key());
            }
        } catch (SQLException e) {
            player.sendSystemMessage(Component.literal("Der Cosmetic-Status konnte nicht gespeichert werden.").withStyle(ChatFormatting.RED));
            e.printStackTrace();
        }
    }

    private final class CosmeticMenu extends AbstractContainerMenu {
        private final ServerPlayer player;
        private final SimpleContainer container;
        private final List<CosmeticEntry> entries;

        private CosmeticMenu(int syncId, Inventory inventory, ServerPlayer player, List<CosmeticEntry> entries) {
            super(MenuType.GENERIC_9x3, syncId);
            this.player = player;
            this.container = new SimpleContainer(COSMETICS_PER_PAGE);
            this.entries = new ArrayList<>(entries.subList(0, Math.min(entries.size(), COSMETICS_PER_PAGE)));
            container.startOpen(player);
            for (int row = 0; row < ROWS; row++) {
                for (int column = 0; column < 9; column++) {
                    addSlot(new Slot(container, column + row * 9, 8 + column * 18, 18 + row * 18));
                }
            }
            addStandardInventorySlots(inventory, 8, 85);
            refreshItems();
        }

        private void refreshItems() {
            for (int slot = 0; slot < COSMETICS_PER_PAGE; slot++) {
                if (slot >= entries.size()) {
                    slots.get(slot).set(ItemStack.EMPTY);
                    continue;
                }
                CosmeticEntry entry = entries.get(slot);
                ItemStack stack = new ItemStack(itemFor(entry.key()));
                String status = entry.equipped() ? " (Aktiv)" : " (Deaktiviert)";
                stack.set(DataComponents.CUSTOM_NAME, Component.literal(displayName(entry.key()) + status)
                        .withStyle(entry.equipped() ? ChatFormatting.GREEN : ChatFormatting.GRAY));
                slots.get(slot).set(stack);
            }
        }

        @Override
        public void clicked(int slot, int button, ContainerInput clickType, Player clickedBy) {
            if (slot >= 0 && slot < entries.size()) {
                CosmeticEntry entry = entries.get(slot);
                boolean equipped = !entry.equipped();
                setEquipped(player, entry, equipped);
                entries.set(slot, new CosmeticEntry(entry.key(), equipped));
                refreshItems();
                broadcastChanges();
                return;
            }
            if (slot < COSMETICS_PER_PAGE) return;
            super.clicked(slot, button, clickType, clickedBy);
        }

        @Override
        public ItemStack quickMoveStack(Player player, int slot) {
            // Cosmetic items are buttons, not physical items that can be moved.
            return ItemStack.EMPTY;
        }

        @Override
        public boolean stillValid(net.minecraft.world.entity.player.Player player) {
            return player == this.player && player.isAlive();
        }

        @Override
        public void removed(Player player) {
            super.removed(player);
            container.stopOpen(player);
        }
    }

    private static Item itemFor(String key) {
        return "soultrails".equals(key) ? Items.SOUL_SOIL : Items.PAPER;
    }

    private static String displayName(String key) {
        return "soultrails".equals(key) ? "Soul Trails" : key;
    }

}
