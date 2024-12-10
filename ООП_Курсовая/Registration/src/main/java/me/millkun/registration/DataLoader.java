package me.millkun.registration;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public class DataLoader {

    private final PlayerDataManager playerDataManager;
    private FileConfiguration ranksConfig;
    private File ranksFile;

    public DataLoader(PlayerDataManager playerDataManager) {
        this.playerDataManager = playerDataManager;
        this.ranksFile = new File("D:/jeb32/Server-1.12.2-ETU/ranks.yml");
    }

    public void loadRanks() {
        if (!ranksFile.exists()) {
            ranksFile.getParentFile().mkdirs();
            try {
                ranksFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ranksConfig = YamlConfiguration.loadConfiguration(ranksFile);

        for (String key : ranksConfig.getConfigurationSection("ranks").getKeys(false)) {
            UUID uuid = UUID.fromString(key);
            String rank = ranksConfig.getString("ranks." + key);
            playerDataManager.getPlayerRanks().put(uuid, rank);
        }

        for (String key : ranksConfig.getConfigurationSection("passwords").getKeys(false)) {
            UUID uuid = UUID.fromString(key);
            String password = ranksConfig.getString("passwords." + key);
            playerDataManager.getPlayerPasswords().put(uuid, password);
        }
    }

    public void saveRanks() {
        for (Map.Entry<UUID, String> entry : playerDataManager.getPlayerRanks().entrySet()) {
            ranksConfig.set("ranks." + entry.getKey().toString(), entry.getValue());
        }

        for (Map.Entry<UUID, String> entry : playerDataManager.getPlayerPasswords().entrySet()) {
            ranksConfig.set("passwords." + entry.getKey().toString(), entry.getValue());
        }

        try {
            ranksConfig.save(ranksFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
