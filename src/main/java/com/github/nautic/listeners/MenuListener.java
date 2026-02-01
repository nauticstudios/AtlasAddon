package com.github.nautic.listeners;

import com.github.nautic.api.AtlasAPI;

import com.github.nautic.menu.MenuManager;
import com.github.nautic.utils.addColor;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MenuListener implements Listener {

    private static final String MENU_PREFIX = "Languages";

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player player)) return;
        if (e.getView().getTitle() == null) return;

        String title = ChatColor.stripColor(e.getView().getTitle());
        if (!title.startsWith(MENU_PREFIX)) return;

        e.setCancelled(true);

        if (e.getClickedInventory() == null) return;
        if (!e.getClickedInventory().equals(e.getView().getTopInventory())) return;

        ItemStack item = e.getCurrentItem();
        if (item == null || !item.hasItemMeta()) return;

        int slot = e.getSlot();
        int page = getPage(title);

        if (slot == 26) {
            MenuManager.open(player, page + 1);
            return;
        }

        if (slot == 18) {
            MenuManager.open(player, page - 1);
            return;
        }

        String name = ChatColor.stripColor(item.getItemMeta().getDisplayName());
        String lang = name.toLowerCase();

        if (AtlasAPI.setLanguage(player, lang)) {
            player.sendMessage(addColor.Set(
                    AtlasAPI.getAddon(player, "messages.selected").replace("<lang>", lang)
            ));
            player.closeInventory();
        } else {
            player.sendMessage(addColor.Set(
                    AtlasAPI.getAddon(player, "messages.invalid").replace("<lang>", lang)
            ));
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent e) {
        if (e.getView().getTitle() == null) return;
        String title = ChatColor.stripColor(e.getView().getTitle());
        if (title.startsWith(MENU_PREFIX)) {
            e.setCancelled(true);
        }
    }

    private int getPage(String title) {
        try {
            String[] split = title.split(">");
            String[] pages = split[1].trim().split("/");
            return Integer.parseInt(pages[0]);
        } catch (Exception e) {
            return 1;
        }
    }
}