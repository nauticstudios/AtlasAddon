package com.github.nautic.listeners;

import com.github.nautic.menu.MenuManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class FirstJoinListener implements Listener {

    @EventHandler
    public void onFirstJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        if (!player.hasPlayedBefore()) {
            Bukkit.getScheduler().runTaskLater(
                    Bukkit.getPluginManager().getPlugin("AtlasAddon"),
                    () -> MenuManager.open(player, 1),
                    20L
            );
        }
    }
}