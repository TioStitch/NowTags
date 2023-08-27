package de.tiostitch.tags.data;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class YAMLData {
    public static void setTagAtual(String playerName, String i) throws IOException {
        File file = new File("plugins/NowTags/data/" + playerName + "/data.yml");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        yamlConfiguration.set("principal.tag_atual", String.valueOf(i));
        yamlConfiguration.save(file);
    }

    public static String getTagAtual(String playerName) {
        File file = new File("plugins/NowTags/data/" + playerName + "/data.yml");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        return yamlConfiguration.getString("principal.tag_atual");
    }

    public static List<String> getTags(String playerUUID) {
        File file = new File("plugins/NowTags/data/" + playerUUID + "/data.yml");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        return yamlConfiguration.getStringList("principal.tags");
    }

    public static void addTag(String playerName, String i) throws IOException {
        File file = new File("plugins/NowTags/data/" + playerName + "/data.yml");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        List<String> lista = yamlConfiguration.getStringList("principal.tags");
        lista.add(i);
        yamlConfiguration.set("principal.tags", lista);
        yamlConfiguration.save(file);
    }

    public static void removeTag(String playerName, String i) throws IOException {
        File file = new File("plugins/NowTags/data/" + playerName + "/data.yml");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        List<String> lista = yamlConfiguration.getStringList("principal.tags");
        lista.remove(i);
        yamlConfiguration.set("principal.tags", lista);
        yamlConfiguration.save(file);
    }

    public static void createPlayer(Player p) throws IOException {
        File folder = new File("plugins/NowTags/data/" + p.getName());
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File playerFile = new File("plugins/NowTags/data/" + p.getName() + "/data.yml");
        if (!playerFile.exists()) {
            playerFile.createNewFile();
            YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(playerFile);

            yamlConfiguration.set("principal.tag_atual", "");
            yamlConfiguration.set("principal.tags", "");

            yamlConfiguration.save(playerFile);
        }
    }
}

