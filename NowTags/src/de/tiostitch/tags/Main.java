package de.tiostitch.tags;

import de.tiostitch.tags.comandos.TagsCMD;
import de.tiostitch.tags.data.PlayerData;
import de.tiostitch.tags.eventos.PlayerEvents;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public class Main extends JavaPlugin {

    private static Main plugin;
    public static PlayerData data;

    @Override
    public void onEnable() {
        plugin = this;

        Bukkit.getConsoleSender().sendMessage("§a--------------------");
        Bukkit.getConsoleSender().sendMessage("§b§l     NowTags");
        Bukkit.getConsoleSender().sendMessage("§a>  §7Autor: §aTioStitch");
        Bukkit.getConsoleSender().sendMessage("§a>  §7Versão: §a1.0");
        Bukkit.getConsoleSender().sendMessage("§a");
        Bukkit.getConsoleSender().sendMessage("§a> Iniciado com Sucesso!");
        Bukkit.getConsoleSender().sendMessage("§a--------------------");

        Bukkit.getPluginManager().registerEvents(new PlayerEvents(), this);
        Bukkit.getPluginCommand("tags").setExecutor(new TagsCMD());
        saveDefaultConfig();

        data = new PlayerData(
                getConfig().getString("database.host"),
                getConfig().getInt("database.port"),
                getConfig().getString("database.database"),
                getConfig().getString("database.username"),
                getConfig().getString("database.password")
        );
        try {
            data.connect();
            data.createTable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisable() {

        try {
            data.disconnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Bukkit.getConsoleSender().sendMessage("§a--------------------");
        Bukkit.getConsoleSender().sendMessage("§b§l     NowTags");
        Bukkit.getConsoleSender().sendMessage("§a>  §7Autor: §aTioStitch");
        Bukkit.getConsoleSender().sendMessage("§a>  §7Versão: §a1.0");
        Bukkit.getConsoleSender().sendMessage("§a");
        Bukkit.getConsoleSender().sendMessage("§c> Desligado com Sucesso!");
        Bukkit.getConsoleSender().sendMessage("§a--------------------");

    }

    public static Main getPlugin() {
        return plugin;
    }
}
