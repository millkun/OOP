package me.millkun.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WarpManager {

    private Commands plugin;
    private Map<String, Location> warps = new HashMap<>();

    public WarpManager(Commands plugin) {
        this.plugin = plugin;
        loadWarps();
    }

    private void loadWarps() {
        File file = new File(plugin.getDataFolder(), "warps.yml");
        if (!file.exists()) {
            plugin.saveResource("warps.yml", false);
        }
        FileConfiguration warpsConfig = YamlConfiguration.loadConfiguration(file);
        for (String key : warpsConfig.getKeys(false)) {
            double x = warpsConfig.getDouble(key + ".x");
            double y = warpsConfig.getDouble(key + ".y");
            double z = warpsConfig.getDouble(key + ".z");
            String worldName = warpsConfig.getString(key + ".world");
            Location location = new Location(Bukkit.getWorld(worldName), x, y, z);
            warps.put(key, location);
        }
    }

    public void saveWarps() {
        File file = new File(plugin.getDataFolder(), "warps.yml");
        FileConfiguration warpsConfig = YamlConfiguration.loadConfiguration(file);
        for (Map.Entry<String, Location> entry : warps.entrySet()) {
            String key = entry.getKey();
            Location loc = entry.getValue();
            warpsConfig.set(key + ".x", loc.getX());
            warpsConfig.set(key + ".y", loc.getY());
            warpsConfig.set(key + ".z", loc.getZ());
            warpsConfig.set(key + ".world", loc.getWorld().getName());
        }
        try {
            warpsConfig.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Location getWarp(String name) {
        return warps.get(name);
    }

    public void setWarp(String name, Location location) {
        warps.put(name, location);
        saveWarps();
    }

    public boolean deleteWarp(String name) {
        if (warps.containsKey(name)) {
            warps.remove(name);
            saveWarps();
            return true;
        }
        return false;
    }

    public Map<String, Location> getWarps() {
        return warps;
    }
}
