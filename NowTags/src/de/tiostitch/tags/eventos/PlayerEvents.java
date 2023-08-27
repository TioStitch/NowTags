package de.tiostitch.tags.eventos;

import de.tiostitch.tags.Main;
import de.tiostitch.tags.data.YAMLData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.IOException;
import java.sql.SQLException;

import static org.bukkit.Bukkit.getServer;

public class PlayerEvents implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) throws IOException {
        Player player = e.getPlayer();

        switch (Main.getType) {
            case "MYSQL":
                BukkitScheduler scheduler = getServer().getScheduler();
                scheduler.runTaskAsynchronously(Main.getPlugin(), () -> {
                    try {
                        Main.data.createPlayer(player.getUniqueId().toString(), player.getName());
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                });
                break;
            case "YAML":
                YAMLData.createPlayer(player);
        }
    }
}
