package club.selbsthilfe.kuscheltiermafia.displayplugin;

import club.selbsthilfe.kuscheltiermafia.CosmeticEntry;
import club.selbsthilfe.kuscheltiermafia.CosmeticRepository;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CosmeticMenuHandler implements Listener {
    private static final int MENU_SIZE = 27;

    private final JavaPlugin plugin;
    private final CosmeticRepository cosmeticRepository;
    private final Map<UUID, CosmeticManager> cosmeticManagers;

    public CosmeticMenuHandler(JavaPlugin plugin, CosmeticRepository cosmeticRepository,
                               Map<UUID, CosmeticManager> cosmeticManagers) {
        this.plugin = plugin;
        this.cosmeticRepository = cosmeticRepository;
        this.cosmeticManagers = cosmeticManagers;
    }

    public void open(Player player) {
        List<CosmeticEntry> entries;
        try {
            entries = cosmeticRepository.loadWardrobe(player.getUniqueId());
        } catch (SQLException e) {
            player.sendMessage(Component.text("Die Cosmetics konnten nicht geladen werden.", NamedTextColor.RED));
            plugin.getLogger().severe("Could not load cosmetics for " + player.getName() + ": " + e.getMessage());
            return;
        }

        CosmeticMenuHolder holder = new CosmeticMenuHolder(entries);
        Inventory inventory = Bukkit.createInventory(holder, MENU_SIZE, Component.text("Cosmetics"));
        holder.setInventory(inventory);
        refresh(inventory, holder.entries());
        player.openInventory(inventory);
    }

    @EventHandler
    private void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!(event.getView().getTopInventory().getHolder(false) instanceof CosmeticMenuHolder holder)) return;

        event.setCancelled(true);
        int slot = event.getRawSlot();
        if (slot < 0 || slot >= holder.entries().size() || slot >= MENU_SIZE) return;

        CosmeticEntry entry = holder.entries().get(slot);
        boolean equipped = !entry.equipped();
        try {
            if (!cosmeticRepository.setEquipped(player.getUniqueId(), entry.key(), equipped)) return;
        } catch (SQLException e) {
            player.sendMessage(Component.text("Der Cosmetic-Status konnte nicht gespeichert werden.", NamedTextColor.RED));
            plugin.getLogger().severe("Could not update cosmetic " + entry.key() + ": " + e.getMessage());
            return;
        }

        holder.entries().set(slot, new CosmeticEntry(entry.key(), equipped));
        CosmeticManager manager = cosmeticManagers.get(player.getUniqueId());
        if (manager != null) {
            manager.cosmetics.remove(entry.key());
            if (equipped) manager.cosmetics.add(entry.key());
        }
        refresh(event.getView().getTopInventory(), holder.entries());
    }

    private void refresh(Inventory inventory, List<CosmeticEntry> entries) {
        inventory.clear();
        for (int slot = 0; slot < Math.min(entries.size(), MENU_SIZE); slot++) {
            CosmeticEntry entry = entries.get(slot);
            Material material = "soultrails".equals(entry.key()) ? Material.SOUL_SOIL : Material.PAPER;
            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();
            meta.displayName(Component.text(displayName(entry.key()) + (entry.equipped() ? " (Aktiv)" : " (Deaktiviert)"),
                    entry.equipped() ? NamedTextColor.GREEN : NamedTextColor.GRAY));
            item.setItemMeta(meta);
            inventory.setItem(slot, item);
        }
    }

    private static String displayName(String key) {
        return "soultrails".equals(key) ? "Soul Trails" : key;
    }

    private static final class CosmeticMenuHolder implements InventoryHolder {
        private final List<CosmeticEntry> entries;
        private Inventory inventory;

        private CosmeticMenuHolder(List<CosmeticEntry> entries) {
            this.entries = new ArrayList<>(entries.subList(0, Math.min(entries.size(), MENU_SIZE)));
        }

        private List<CosmeticEntry> entries() {
            return entries;
        }

        private void setInventory(Inventory inventory) {
            this.inventory = inventory;
        }

        @Override
        public Inventory getInventory() {
            return inventory;
        }
    }
}
