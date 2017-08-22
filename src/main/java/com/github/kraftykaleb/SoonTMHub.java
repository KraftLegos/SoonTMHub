package com.github.kraftykaleb;

import com.github.kraftykaleb.commands.SetSpawn;
import com.github.kraftykaleb.listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.*;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Kraft on 5/6/2017.
 */
public class SoonTMHub extends JavaPlugin {

    public Connection connection;
    public HashMap<String, Integer> skyflagcoins;
    public HashMap<String, Integer> skyflagwins;
    public HashMap<String, Integer> skyflagkills;
    public HashMap<String, String> guildranks;
    public HashMap<String, String> guilds;
    public HashMap<String, String> hypixelranks;

    public void onEnable() {

        skyflagcoins = new HashMap<>();
        skyflagwins = new HashMap<>();
        skyflagkills = new HashMap<>();
        guildranks = new HashMap<>();
        guilds = new HashMap<>();
        hypixelranks = new HashMap<>();

        createConfig();

        PluginManager pm = Bukkit.getServer().getPluginManager();

        pm.registerEvents(new onJoin(this), this);
        pm.registerEvents(new onLeave(), this);
        pm.registerEvents(new onBlockPlace(this), this);
        pm.registerEvents(new onBlockBreak(this), this);
        pm.registerEvents(new onHealthChange(), this);
        pm.registerEvents(new onFoodChange(), this);
        pm.registerEvents(new onChat(), this);

        getCommand("setspawn").setExecutor(new SetSpawn(this));

    }

    public void onDisable() {
        try {
            if (connection != null || connection.isClosed()) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createConfig() {
        try {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs();
            }
            File file = new File(getDataFolder(), "config.yml");
            if (!file.exists()) {
                getLogger().info("Config.yml not found, creating!");
                saveDefaultConfig();
            } else {
                getLogger().info("Config.yml found, loading!");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public synchronized void openConnection() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://172.106.202.99:3306/Kraft_SoonTMDatabase", "Kraft", "KraftLegos11");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void closeConnection() {
        try {
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized boolean playerDataContainsPlayer(Player player) {
        try {
            PreparedStatement sql = connection.prepareStatement("SELECT * FROM `player_data` WHERE player=?;");
            sql.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = sql.executeQuery();

            boolean containsPlayer = resultSet.next();

            sql.close();
            resultSet.close();

            return containsPlayer;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void loadSkyflagCoins(String name) {
        UUID playerUUID = Bukkit.getServer().getPlayer(name).getUniqueId();

        try {
            PreparedStatement skyflagCoinsStatement = connection.prepareStatement("SELECT skyflag_coins FROM `player_data` WHERE player=?;");
            skyflagCoinsStatement.setString(1, playerUUID.toString());

            ResultSet resultCoinsSet = skyflagCoinsStatement.executeQuery();
            resultCoinsSet.next();

            skyflagcoins.put(name, resultCoinsSet.getInt("skyflag_coins"));

            skyflagCoinsStatement.close();
            resultCoinsSet.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
