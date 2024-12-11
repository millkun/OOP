package me.millkun.ranks;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RankManager {
    private final Ranks plugin;
    private final Map<UUID, String> playerRanks = new HashMap<>();
    private File ranksFile;
    private FileConfiguration ranksConfig;

    public RankManager(Ranks plugin) {
        this.plugin = plugin;
        loadRanks();
    }

    public void loadRanks() {
        // Указываем полный путь к файлу ranks.yml
        ranksFile = new File("D:\\jeb32\\Server-1.12.2-ETU\\ranks.yml");
        if (!ranksFile.exists()) {
            try {
                ranksFile.getParentFile().mkdirs();
                ranksFile.createNewFile(); // Создаем файл, если он не существует
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ranksConfig = YamlConfiguration.loadConfiguration(ranksFile);

        // Загружаем ранги из файла
        for (String key : ranksConfig.getConfigurationSection("ranks").getKeys(false)) {
            UUID uuid = UUID.fromString(key);
            String rank = ranksConfig.getString("ranks." + key);
            playerRanks.put(uuid, rank);
        }
    }

    public void saveRanks() {
        // Сохраняем ранги в файл
        for (Map.Entry<UUID, String> entry : playerRanks.entrySet()) {
            ranksConfig.set("ranks." + entry.getKey().toString(), entry.getValue());
        }
        try {
            ranksConfig.save(ranksFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPlayerRank(Player player, String rank) {
        playerRanks.put(player.getUniqueId(), rank);
        // Update display name logic can be added here
    }

    public String getPlayerRank(Player player) {
        return playerRanks.getOrDefault(player.getUniqueId(), "Пионер");
    }
}