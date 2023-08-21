package de.tiostitch.tags.comandos;

import de.tiostitch.tags.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
public class TagsCMD implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        String prefixo = Main.getPlugin().getConfig().getString("messages.prefixo").replace("&", "§");

        if (sender instanceof Player) {
            if (!sender.hasPermission("NowEssentials.comandos.ajuda")) {
                return false;
            }

            if (args.length == 0) {
                sender.sendMessage("§b§lNowTags §eUtilize /tags ajuda");
                return false;
            }
            if (args[0].equalsIgnoreCase("ajuda")) {
                sender.sendMessage("§b§lNowTags");
                sender.sendMessage("§eComandos de Ajuda:");
                sender.sendMessage("§e/tags usar <tag>");
                sender.sendMessage("§e/tags remover <jogador> <tag>");
                sender.sendMessage("§e/tags adicionar <jogador> tag>");
                sender.sendMessage("§c/tags listar - Indisponível");
            }
        }

        if (args.length == 0) {
            sender.sendMessage(prefixo + " §eUtilize /tags ajuda");
            return false;
        }


        if (args[0].equals("reload")) {

            sender.sendMessage(prefixo + " §aPlugin reiniciado com sucesso!");
            Main.getPlugin().reloadConfig();
        }

        if (args[0].equalsIgnoreCase("usar")) {
            if (sender.hasPermission("NowTags.comandos.usar")) {
                if (args.length == 2) {
                    String tag = String.valueOf(args[1]);

                    if (Main.data.playerTags(sender.getName()).stream().anyMatch(s -> s.equals(tag))) {
                        List<Map<?, ?>> tagsf = Main.getPlugin().getConfig().getMapList("settings.formatted-tags");
                        for (Map<?, ?> item : tagsf) {
                            String name = (String) item.get("name");
                            if (name.equals(tag)) {
                                String fTag = (String) item.get("color");

                                String delTag;
                                try {
                                    delTag = "/" + Main.getPlugin().getConfig().getString("settings.tag-remove-cmd")
                                            .replace("%player%", sender.getName()).replace("%tag%", Main.data.getPlayerTag(sender.getName()));
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), delTag);

                                String putTag = "/" + Main.getPlugin().getConfig().getString("settings.tag-apply-cmd")
                                        .replace("%player%", sender.getName()).replace("%tag%", tag);
                                Bukkit.getServer().dispatchCommand(sender, putTag);

                                Main.data.setPlayerTag(sender.getName(), tag);
                                sender.sendMessage(Main.getPlugin().getConfig().getString("messages.tagChange")
                                        .replace("%player%", sender.getName())
                                        .replace("%next_tag%", fTag)
                                        .replace("&", "§")
                                        .replace("%prefix%", prefixo));
                                return false;
                            }
                        }
                    } else {
                        sender.sendMessage(Main.getPlugin().getConfig().getString("messages.dontHas")
                                .replace("%tag%", tag)
                                .replace("%prefix%", prefixo)
                                .replace("&", "§"));
                    }
                } else {
                    sender.sendMessage("§b§lNowTags §eUtilize /tags usar <tag>");
                    return false;
                }
            } else {
                sender.sendMessage("§cVocê não tem permissão para isto!");
            }
        }

        if (args[0].equalsIgnoreCase("adicionar")) {
            if (sender.hasPermission("NowTags.comandos.adicionar")) {

                if (args.length == 3) {
                    Player player = Bukkit.getPlayer(args[1]);
                    String tag = args[2];

                    if (player == null) {
                        sender.sendMessage(prefixo + " §cDesculpe, você solicitou um jogador inválido!");
                        return false;
                    }

                    List<Map<?, ?>> tagsf = Main.getPlugin().getConfig().getMapList("settings.formatted-tags");
                    for (Map<?, ?> item : tagsf) {
                        String name = (String) item.get("name");
                        if (name.equals(tag)) {
                            String fTag = (String) item.get("color");

                            Main.data.addPlayerTag(player.getName(), tag);
                            player.sendMessage(Main.getPlugin().getConfig().getString("messages.tagadd")
                                    .replace("%player%", player.getName())
                                    .replace("%next_tag%", fTag)
                                    .replace("&", "§")
                                    .replace("%prefix%", prefixo));
                            return false;
                        }
                    }
                } else {
                    sender.sendMessage(prefixo + " §eUtilize /tags adicionar <jogador> <tag>");
                    return false;
                }
            } else {
                sender.sendMessage("§cVocê não tem permissão para isto!");
            }
        }

        if (args[0].equalsIgnoreCase("remover")) {
            if (sender.hasPermission("NowTags.comandos.adicionar")) {

                if (args.length == 3) {
                    Player player = Bukkit.getPlayer(args[1]);
                    String tag = args[2];

                    if (player == null) {
                        sender.sendMessage(prefixo + " §cDesculpe, você solicitou um jogador inválido!");
                        return false;
                    }

                    List<Map<?, ?>> tagsf = Main.getPlugin().getConfig().getMapList("settings.formatted-tags");
                    for (Map<?, ?> item : tagsf) {
                        String name = (String) item.get("name");
                        if (name.equals(tag)) {
                            String fTag = (String) item.get("color");

                            Main.data.removePlayerTag(player.getName(), tag);
                            player.sendMessage(Main.getPlugin().getConfig().getString("messages.tagremove")
                                    .replace("%player%", player.getName())
                                    .replace("%next_tag%", fTag)
                                    .replace("&", "§")
                                    .replace("%prefix%", prefixo));
                            return false;
                        }
                    }
                } else {
                    sender.sendMessage(prefixo + " §eUtilize /tags remover <jogador> <tag>");
                    return false;
                }
            } else {
                sender.sendMessage("§cVocê não tem permissão para isto!");
            }
        }

        return false;
    }
}
