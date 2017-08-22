package com.github.kraftykaleb.listeners;

import com.github.kraftykaleb.SoonTMHub;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

/**
 * Created by Kraft on 5/7/2017.
 */
public class onBlockBreak implements Listener {
    private SoonTMHub plugin;

    public onBlockBreak(SoonTMHub instance) {
        plugin = instance;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (plugin.guilds.get(p.getName()).equals("SoonTM")) {
            if (!plugin.guildranks.get(p.getName()).equals("MEMBER")) {
                e.setCancelled(false);
            } else {
                e.setCancelled(true);
            }
        } else {
            e.setCancelled(true);
        }
    }
}
