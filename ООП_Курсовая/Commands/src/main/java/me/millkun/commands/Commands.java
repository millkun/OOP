package me.millkun.commands;

import org.bukkit.plugin.java.JavaPlugin;

public class Commands extends JavaPlugin {

    private RankManager rankManager;
    private WarpManager warpManager;
    private CommandHandler commandHandler;

    @Override
    public void onEnable() {
        rankManager = new RankManager(this);
        warpManager = new WarpManager(this);
        commandHandler = new CommandHandler(this, rankManager, warpManager);

        // Регистрация команд
        this.getCommand("gamemode").setExecutor(commandHandler);
        this.getCommand("setwarp").setExecutor(commandHandler);
        this.getCommand("delwarp").setExecutor(commandHandler);
        this.getCommand("warp").setExecutor(commandHandler);
        this.getCommand("warps").setExecutor(commandHandler);
        this.getCommand("spawn").setExecutor(commandHandler);
        this.getCommand("sun").setExecutor(commandHandler);
        this.getCommand("rain").setExecutor(commandHandler);
        this.getCommand("thunder").setExecutor(commandHandler);
        this.getCommand("day").setExecutor(commandHandler);
        this.getCommand("night").setExecutor(commandHandler);
    }

    @Override
    public void onDisable() {
        warpManager.saveWarps();
    }
}
