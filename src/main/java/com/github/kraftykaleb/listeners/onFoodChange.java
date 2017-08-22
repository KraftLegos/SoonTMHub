package com.github.kraftykaleb.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

/**
 * Created by Kraft on 5/7/2017.
 */
public class onFoodChange implements Listener {
    @EventHandler
    public void foodChange(FoodLevelChangeEvent e) {
        Player p = (Player) e.getEntity();
        p.setFoodLevel(20);
    }
}
