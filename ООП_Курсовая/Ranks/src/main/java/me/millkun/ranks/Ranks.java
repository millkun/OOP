package me.millkun.ranks;

import org.bukkit.plugin.java.JavaPlugin;

public final class Ranks extends JavaPlugin {

    private RankManager rankManager;
    private AnimationManager animationManager;

    @Override
    public void onEnable() {
        rankManager = new RankManager(this);
        animationManager = new AnimationManager(this, rankManager);
        getCommand("setrank").setExecutor(new RankCommandExecutor(rankManager));
    }

    @Override
    public void onDisable() {
        rankManager.saveRanks();
    }
}