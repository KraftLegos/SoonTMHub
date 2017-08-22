package com.github.kraftykaleb.commands;

import com.github.kraftykaleb.SoonTMHub;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Kraft on 5/7/2017.
 */
public class SetSpawn implements CommandExecutor {

    private SoonTMHub plugin;

    public SetSpawn(SoonTMHub instance) {
        plugin = instance;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (plugin.guilds.get(sender.getName()).equals("SoonTM")) {
            if (!plugin.guilds.get(sender.getName()).equals("MEMBER")) {
                Player p = (Player) sender;
                String world = p.getLocation().getWorld().getName();
                Double x = p.getLocation().getX();
                Double y = p.getLocation().getY();
                Double z = p.getLocation().getZ();
                Float pitch = p.getLocation().getPitch();
                Float yaw = p.getLocation().getYaw();
                plugin.getConfig().set("spawnlocation.world", world);
                plugin.getConfig().set("spawnlocation.x", x);
                plugin.getConfig().set("spawnlocation.y", y);
                plugin.getConfig().set("spawnlocation.z", z);
                plugin.getConfig().set("spawnlocation.pitch", pitch);
                plugin.getConfig().set("spawnlocation.yaw", yaw);
                plugin.saveConfig();
                return true;
            }
        }
        sender.sendMessage(ChatColor.RED + "You do not have permission to use that command!");
        return true;
    }
}
