package com.github.kraftykaleb.listeners;

import com.github.kraftykaleb.SoonTMHub;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Created by Kraft on 5/7/2017.
 */
public class onJoin implements Listener {

    public Map<UUID, Scoreboard> scoreboardList = new HashMap<>();
    private SoonTMHub plugin;

    public onJoin(SoonTMHub instance) {
        plugin = instance;
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        e.setJoinMessage(null);

        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    player.setFoodLevel(20);
                }
            }
        }, 0L,7*20L);
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                plugin.openConnection();
                loadPlayer(p);
                String prefix = getRankPrefix(p);

                ScoreboardManager manager = Bukkit.getScoreboardManager();
                Scoreboard board = manager.getNewScoreboard();
                scoreboardList.put(p.getUniqueId(), board);

                Objective o = scoreboardList.get(p.getUniqueId()).registerNewObjective("line", "dummy");
                o.setDisplaySlot(DisplaySlot.SIDEBAR);
                o.setDisplayName("§6§l§oSkyFlag");

                Score line1 = o.getScore(" ");
                line1.setScore(7);

                Score line2 = o.getScore("Kills: §a" + plugin.skyflagkills.get(p.getName()));
                line2.setScore(6);

                Score line3 = o.getScore("  ");
                line3.setScore(5);

                Score line4 = o.getScore("Wins: §a" + plugin.skyflagwins.get(p.getName()));
                line4.setScore(4);

                Score line5 = o.getScore("   ");
                line5.setScore(3);

                Score line6 = o.getScore("Coins: §a" + plugin.skyflagcoins.get(p.getName()));
                line6.setScore(2);

                Score line7 = o.getScore("    ");
                line7.setScore(1);

                p.setScoreboard(scoreboardList.get(p.getUniqueId()));

                p.setPlayerListName(prefix + p.getName());
                if (plugin.getConfig().getString("spawnlocation.world") != null) {

                    String world = plugin.getConfig().getString("spawnlocation.world");
                    Double x = plugin.getConfig().getDouble("spawnlocation.x");
                    Double y = plugin.getConfig().getDouble("spawnlocation.y");
                    Double z = plugin.getConfig().getDouble("spawnlocation.z");
                    float pitch = plugin.getConfig().getInt("spawnlocation.pitch");
                    float yaw = plugin.getConfig().getInt("spawnlocation.yaw");

                    Location spawn = new Location(Bukkit.getServer().getWorld(world), x, y, z);
                    spawn.setPitch(pitch);
                    spawn.setYaw(yaw);

                    p.teleport(spawn);
                } else {
                    Bukkit.getLogger().log(Level.SEVERE, "YOU HAVE NOT SET UP A SPAWN LOCATION YET. TELEPORTING ALL PLAYERS TO SPAWN");
                }


                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {

                        if (plugin.hypixelranks.containsKey(p.getName())) {
                            if (plugin.hypixelranks.get(p.getName()).equals("MVP_PLUS")) {
                                Bukkit.getServer().broadcastMessage(p.getDisplayName() + ChatColor.GOLD + " has joined the lobby!");
                            }
                            if (!(plugin.hypixelranks.get(p.getName()).equals("DEFAULT"))) {
                                p.setAllowFlight(true);
                                p.setFlying(true);

                            }
                        }

                    }
                }, 10L);
            }
        }, 20L);
    }

    public void loadPlayer(Player p) {
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
        try {
            if (plugin.playerDataContainsPlayer(p)) {
                PreparedStatement skyflagwins = plugin.connection.prepareStatement("SELECT skyflag_wins FROM `player_data` WHERE player=?;");
                skyflagwins.setString(1, p.getUniqueId().toString());

                ResultSet resultWinsSet = skyflagwins.executeQuery();
                resultWinsSet.next();

                plugin.skyflagwins.put(p.getName(), resultWinsSet.getInt("skyflag_wins"));

                PreparedStatement skyflagkills = plugin.connection.prepareStatement("SELECT skyflag_kills FROM `player_data` WHERE player=?;");
                skyflagkills.setString(1, p.getUniqueId().toString());

                ResultSet resultKillsSet = skyflagkills.executeQuery();
                resultKillsSet.next();

                plugin.skyflagkills.put(p.getName(), resultKillsSet.getInt("skyflag_kills"));

                PreparedStatement guildrank = plugin.connection.prepareStatement("SELECT rank_guild FROM `player_data` WHERE player=?");
                guildrank.setString(1, p.getUniqueId().toString());

                ResultSet resultGuildRank = guildrank.executeQuery();
                resultGuildRank.next();

                plugin.guildranks.put(p.getName(), resultGuildRank.getString("rank_guild"));

                PreparedStatement guild = plugin.connection.prepareStatement("SELECT guild FROM `player_data` WHERE player=?");
                guild.setString(1, p.getUniqueId().toString());

                ResultSet resultGuild = guild.executeQuery();
                resultGuild.next();

                plugin.guilds.put(p.getName(), resultGuild.getString("guild"));

                plugin.loadSkyflagCoins(p.getName());

                PreparedStatement rank = plugin.connection.prepareStatement("SELECT rank FROM `player_data` WHERE player=?");
                rank.setString(1, p.getUniqueId().toString());

                ResultSet resultHypixelRank = rank.executeQuery();
                resultHypixelRank.next();

                plugin.hypixelranks.put(p.getName(), resultHypixelRank.getString("rank"));


                rank.close();
                resultHypixelRank.close();
                guild.close();
                guildrank.close();
                resultGuild.close();
                resultGuildRank.close();
                skyflagkills.close();
                skyflagwins.close();
                resultKillsSet.close();
                resultWinsSet.close();
                Bukkit.getServer().getLogger().log(Level.INFO, p.getName() + " was found and now has " + plugin.skyflagwins.get(p.getName()) + " wins!");
            } else {

                p.kickPlayer(ChatColor.RED + "An SQL error occured, Please report this to a developer! \nSQLPLAYERNOTFOUND AT: " + Thread.currentThread().getStackTrace()[2].getClassName() + "(" + Thread.currentThread().getStackTrace()[2].getMethodName() + "(" + Thread.currentThread().getStackTrace()[2].getLineNumber() + "))");
                //Bukkit.getServer().broadcast(ChatColor.R + "[BOT] SoonTM: " + net.md_5.bungee.api.ChatColor.YELLOW + "Please welcome " + p.getDisplayName() + net.md_5.bungee.api.ChatColor.YELLOW + " to the server!"));
                //ProxyServer.getInstance().getLogger().log(Level.INFO, "Created a new player on the database!");
            }
        } catch (Exception error) {
            error.printStackTrace();
        } finally {
            plugin.closeConnection();
        }

            }
        }, 15L);
    }

    public String getRankPrefix(Player p) {
        if (plugin.hypixelranks.containsKey(p.getName())) {
            if (plugin.hypixelranks.get(p.getName()).equals("ADMIN")) {
                return "§c[ADMIN] ";
            } else if (plugin.hypixelranks.get(p.getName()).equals("MODERATOR")) {
                return "§2[MOD] ";
            } else if (plugin.hypixelranks.get(p.getName()).equals("HELPER")) {
                return "§9[HELPER] ";
            } else if (plugin.hypixelranks.get(p.getName()).equals("MVP_PLUS")) {
                return "§b[MVP§c+§b] ";
            } else if (plugin.hypixelranks.get(p.getName()).equals("MVP")) {
                return "§b[MVP] ";
            } else if (plugin.hypixelranks.get(p.getName()).equals("VIP_PLUS")) {
                return "§a[VIP§6+§a] ";
            } else if (plugin.hypixelranks.get(p.getName()).equals("VIP")) {
                return "§a[VIP] ";
            } else if (plugin.hypixelranks.get(p.getName()).equals("DEFAULT")) {
                return "§7";

            } else {
                return "§7";
            }
        } else {
            return "§7";
        }
    }
}
