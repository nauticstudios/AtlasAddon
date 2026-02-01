package com.github.nautic.menu;

import com.github.nautic.api.AtlasAPI;

import com.github.nautic.utils.addColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.*;

public class MenuManager {

    private static final int[] SLOTS = {
            11,12,13,14,15,
            20,21,22,23,24,
            29,30,31,32,33
    };

    public static void open(Player player, int page) {
        String lang = AtlasAPI.getLanguage(player);

        File file = new File("plugins/AtlasLang/languages/" + lang + "/atlasaddon.yml");
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        Set<String> registered = AtlasAPI.getRegisteredLanguages();
        List<String> languages = new ArrayList<>(registered);

        if (languages.isEmpty()) return;

        int maxPages = (int) Math.ceil(languages.size() / 15.0);
        if (maxPages <= 0) maxPages = 1;
        if (page < 1) page = 1;
        if (page > maxPages) page = maxPages;

        String title = cfg.getString("menu.title", "Languages > <page>/<pages>")
                .replace("<page>", String.valueOf(page))
                .replace("<pages>", String.valueOf(maxPages));

        Inventory inv = Bukkit.createInventory(null, 45, addColor.Set(title));

        int start = (page - 1) * 15;
        int index = start;

        for (int slot : SLOTS) {
            if (index >= languages.size()) break;
            String langKey = languages.get(index);
            inv.setItem(slot, buildLangItem(cfg, langKey));
            index++;
        }

        if (page > 1) {
            inv.setItem(18, buildArrow(cfg, "arrows.previous"));
        }

        if (page < maxPages) {
            inv.setItem(26, buildArrow(cfg, "arrows.next"));
        }

        player.openInventory(inv);
    }

    private static ItemStack buildLangItem(YamlConfiguration cfg, String lang) {
        Material mat = Material.valueOf(cfg.getString("menu.language-item.material", "PAPER"));
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(addColor.Set(
                cfg.getString("menu.language-item.name", "&a<lang>").replace("<lang>", lang)
        ));

        List<String> lore = cfg.getStringList("menu.language-item.lore");
        List<String> colored = new ArrayList<>();
        for (String s : lore) {
            colored.add(addColor.Set(s.replace("<lang>", lang)));
        }

        meta.setLore(colored);
        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack buildArrow(YamlConfiguration cfg, String path) {
        Material mat = Material.valueOf(cfg.getString(path + ".material", "ARROW"));
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(addColor.Set(cfg.getString(path + ".name", "&eNext")));

        List<String> lore = cfg.getStringList(path + ".lore");
        List<String> colored = new ArrayList<>();
        for (String s : lore) {
            colored.add(addColor.Set(s));
        }

        meta.setLore(colored);
        item.setItemMeta(meta);
        return item;
    }
}