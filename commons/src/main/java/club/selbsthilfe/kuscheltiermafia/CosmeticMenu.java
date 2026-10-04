package club.selbsthilfe.kuscheltiermafia;

import java.util.*;

public class CosmeticMenu {
    public record MenuEntry(
            String name,
            String material

    ) {}

    static List<MenuEntry> ALL_MENU_ITEMS = List.of(
            new MenuEntry("Soultrails", "Soul_Soil"),
            new MenuEntry("x", "BLAZE_ROD"),
            new MenuEntry("x", "DIAMOND_BOOTS"),
            new MenuEntry("x", "BOW"),
            new MenuEntry("x", "DIAMOND_PICKAXE")
    );

    public static List<MenuEntry> buildMenu(List<String> playerItems) {
        Set<String> owned = new HashSet<>(playerItems);

        return ALL_MENU_ITEMS.stream()
                .filter(item -> owned.contains(item.name()))
                .toList();
    }
}
