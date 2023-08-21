package de.tiostitch.tags.eventos;

import de.tiostitch.tags.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;

public class PlayerEvents implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) throws SQLException {
        Player player = e.getPlayer();

        Main.data.createPlayer(player.getUniqueId().toString(), player.getName());

    }
}
