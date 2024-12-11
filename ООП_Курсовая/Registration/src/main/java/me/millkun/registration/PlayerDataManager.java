package me.millkun.registration;

import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class PlayerDataManager {

    private final Map<UUID, String> playerRanks = new HashMap<>();
    private final Map<UUID, String> playerPasswords = new HashMap<>();
    private final Map<UUID, Location> playerLocations = new HashMap<>();
    private final Map<UUID, Boolean> loginAttempted = new HashMap<>();

    public boolean isRegistered(UUID uuid) {
        return playerRanks.containsKey(uuid);
    }

    public void registerPlayer(UUID uuid, String password, String rank) {
        playerPasswords.put(uuid, password);
        playerRanks.put(uuid, rank);
    }

    public boolean hasLoginAttempted(UUID uuid) {
        return loginAttempted.getOrDefault(uuid, false);
    }

    public boolean validatePassword(UUID uuid, String password) {
        return playerPasswords.containsKey(uuid) && playerPasswords.get(uuid).equals(password);
    }

    public Optional<Location> getPlayerLocation(UUID uuid) {
        return Optional.ofNullable(playerLocations.get(uuid));
    }

    public void setLoginAttempted(UUID uuid, boolean attempted) {
        loginAttempted.put(uuid, attempted);
    }

    public void savePlayerLocation(UUID uuid, Location location) {
        playerLocations.put(uuid, location);
    }

    public Map<UUID, String> getPlayerRanks() {
        return playerRanks;
    }

    public Map<UUID, String> getPlayerPasswords() {
        return playerPasswords;
    }

    public void setPlayerRanks(Map<UUID, String> playerRanks) {
        this.playerRanks.clear();
        this.playerRanks.putAll(playerRanks);
    }

    public void setPlayerPasswords(Map<UUID, String> playerPasswords) {
        this.playerPasswords.clear();
        this.playerPasswords.putAll(playerPasswords);
    }
}
