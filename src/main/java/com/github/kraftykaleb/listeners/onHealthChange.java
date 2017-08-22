package com.github.kraftykaleb.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Created by Kraft on 5/7/2017.
 */
public class onHealthChange implements Listener {
    @EventHandler
    public void onHealthChange(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            e.setCancelled(true);
            p.setHealth(20);
        }
    }
}
