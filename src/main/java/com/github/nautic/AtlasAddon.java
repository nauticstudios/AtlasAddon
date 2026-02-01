package com.github.nautic;

import com.github.nautic.command.AtlasAddonPreview;
import com.github.nautic.listeners.FirstJoinListener;
import com.github.nautic.listeners.MenuListener;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;

public final class AtlasAddon extends JavaPlugin {

    private static AtlasAddon instance;

    public static AtlasAddon get() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        Plugin atlasLang = Bukkit.getPluginManager().getPlugin("AtlasLang");

        if (atlasLang == null || !atlasLang.isEnabled()) {
            getLogger().severe("======================================");
            getLogger().severe(" AtlasLang is NOT installed or enabled!");
            getLogger().severe(" This plugin requires AtlasLang to work.");
            getLogger().severe(" Please download AtlasLang and restart.");
            getLogger().severe("======================================");

            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        AtlasAddonFileManager.generateForAllLanguages();

        getServer().getPluginManager().registerEvents(new MenuListener(), this);
        getServer().getPluginManager().registerEvents(new FirstJoinListener(), this);

        registerCommand();

        getLogger().info("AtlasAddon enabled successfully!");
    }

    private void registerCommand() {
        try {
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());

            Command cmd = new Command("atlasaddon") {
                @Override
                public boolean execute(CommandSender sender, String label, String[] args) {
                    return new AtlasAddonPreview().onCommand(sender, this, label, args);
                }

                @Override
                public boolean testPermissionSilent(CommandSender sender) {
                    if (!(sender instanceof org.bukkit.entity.Player p)) return false;
                    return p.isOp() || p.hasPermission("atlaslang.admin");
                }
            };

            cmd.setPermission("atlaslang.admin");
            cmd.setDescription("Open AtlasAddon language menu");

            commandMap.register("atlasaddon", cmd);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("AtlasAddon disabled!");
    }
}