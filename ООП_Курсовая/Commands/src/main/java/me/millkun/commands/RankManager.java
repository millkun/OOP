package me.millkun.commands;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class RankManager {

    private Commands plugin;
    private Map<String, String> ranks = new HashMap<>();

    public RankManager(Commands plugin) {
        this.plugin = plugin;
        loadRanks();
    }

    private void loadRanks() {
        // Укажите полный путь к файлу ranks.yml
        File file = new File("D:/jeb32/Server-1.12.2-ETU/ranks.yml");

        // Проверяем, существует ли файл
        if (!file.exists()) {
            plugin.getLogger().warning("Файл ranks.yml не найден по пути: " + file.getAbsolutePath());
            return; // Выходим из метода, если файл не найден
        }

        // Загружаем конфигурацию из файла
        FileConfiguration ranksConfig = YamlConfiguration.loadConfiguration(file);

        // Загружаем ранги из конфигурации
        ranksConfig.getConfigurationSection("ranks").getKeys(false).forEach(uuid -> {
            ranks.put(uuid, ranksConfig.getString("ranks." + uuid));
        });
    }

    public String getRank(String uuid) {
        return ranks.get(uuid);
    }
}
