package com.github.kraftykaleb.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Created by Kraft on 5/7/2017.
 */
public class onChat implements Listener {

    @EventHandler
    public void onChatEvent(AsyncPlayerChatEvent e) {
        e.setCancelled(true);
        Bukkit.getServer().broadcastMessage(e.getPlayer().getDisplayName() + ": " + ChatColor.GRAY + e.getMessage());
    }
}
