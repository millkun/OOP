package me.millkun.registration;

import org.bukkit.plugin.java.JavaPlugin;

public final class RegistrationPlugin extends JavaPlugin {

    private PlayerDataManager playerDataManager;
    private DataLoader dataLoader;

    @Override
    public void onEnable() {
        playerDataManager = new PlayerDataManager();
        dataLoader = new DataLoader(playerDataManager);
        dataLoader.loadRanks();

        getCommand("register").setExecutor(new CommandHandler(this, playerDataManager));
        getCommand("login").setExecutor(new CommandHandler(this, playerDataManager));
        getServer().getPluginManager().registerEvents(new RegistrationEventHandler(this, playerDataManager), this);
    }

    @Override
    public void onDisable() {
        dataLoader.saveRanks();
    }
}
